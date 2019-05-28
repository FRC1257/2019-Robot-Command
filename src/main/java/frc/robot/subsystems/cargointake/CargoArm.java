package frc.robot.subsystems.cargointake;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.RobotMap;
import frc.robot.commands.cargointake.cargoarm.*;

/**
 * Subsystem to handle the arm controlling the cargo intake
 *  - Utilizes a single NEO motor attached to a pivot
 *  - Uses a bump switch to zero the encoder at the bottom of the pivot
 *  - Uses PID to move the arm to specific setpoints
 */
public class CargoArm extends Subsystem {

    // Constants
    public static final double CARGO_ARM_PID_ROCKET = 11.0; // Target position for rocket (revolutions)
    public static final double CARGO_ARM_PID_CARGO = 18.5; // Target position for cargo ship (revolutions)
    public static final double CARGO_ARM_PID_RAISED = 28.0; // Initial position of arm (revolutions)
    public static double CARGO_ARM_MAX_SPEED = 1.0;

    public static final double[] CARGO_ARM_PIDF = { 0.1, 0.0, 0.0, 0.0 };
    public static final double CARGO_ARM_ARB_F = 0.0;
    public static final double CARGO_ARM_ANGLE_CONV_FACTOR = 90.0 / CARGO_ARM_PID_RAISED; // conversion factor from motor rev to angle
    public static final double CARGO_ARM_PID_TOLERANCE = 1.0; // revolutions
    public static final double CARGO_ARM_PID_WAIT = 2.0; // seconds
    public static final double CARGO_ARM_PID_MAX_OUTPUT = 1.0; // percentage
    public static final double CARGO_ARM_PID_MIN_OUTPUT = -1.0; // percentage

    private CANSparkMax cargoArmMotor;
    private CANEncoder cargoArmEncoder;
    private CANPIDController cargoArmPID;

    private DigitalInput limitSwitch;
    private boolean lastLimit;

    private double speed;
    private double currentPIDSetpoint;

    /**
     * MANUAL - Using manual input from the controller to control the motor
     * PID - Using PID control to go to and maintain a specific position
     * FROZEN - Uses PID to maintain the current position of the arm
     */
    public enum State {
        MANUAL, PID, FROZEN
    }
    private State state = State.MANUAL;

    public CargoArm() {
        cargoArmMotor = new CANSparkMax(RobotMap.CARGO_ARM_MOTOR_ID, MotorType.kBrushless);
        cargoArmMotor.restoreFactoryDefaults();
        cargoArmMotor.setIdleMode(IdleMode.kBrake);
        cargoArmMotor.setSmartCurrentLimit(RobotMap.NEO_CURRENT_LIMIT);
        cargoArmEncoder = cargoArmMotor.getEncoder();
        cargoArmPID = cargoArmMotor.getPIDController();
        cargoArmPID.setP(CARGO_ARM_PIDF[0]);
        cargoArmPID.setI(CARGO_ARM_PIDF[1]);
        cargoArmPID.setD(CARGO_ARM_PIDF[2]);
        cargoArmPID.setFF(CARGO_ARM_PIDF[3]);
        cargoArmPID.setIZone(0.0);
        cargoArmPID.setOutputRange(CARGO_ARM_PID_MIN_OUTPUT, CARGO_ARM_PID_MAX_OUTPUT);
        
        limitSwitch = new DigitalInput(RobotMap.CARGO_ARM_LIMIT_SWITCH_ID);
        lastLimit = getLimitSwitch();

        setConstantTuning();
        reset();
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new CargoArmCommand());
    }
    
    private void reset() {
        cargoArmMotor.set(0);

        currentPIDSetpoint = -1257;
        resetEncoderTop();

        state = State.MANUAL;
    }

    /**
     * Update motor outputs according to the current state
     */
    public void update() {
        switch(state) {
            case MANUAL:
                cargoArmMotor.set(speed);
                currentPIDSetpoint = -1257;
            break;
            case PID:
                if(currentPIDSetpoint == -1257) {
                    state = State.MANUAL;
                }
                else {
                    cargoArmPID.setReference(currentPIDSetpoint, ControlType.kPosition, 0,
                        CARGO_ARM_ARB_F * Math.cos(getArmAngle()));

                    double error = Math.abs(getEncoderPosition() - currentPIDSetpoint);
                    if(error < CARGO_ARM_PID_TOLERANCE) {
                        state = State.MANUAL;
                    }
                }   
            break;
            case FROZEN:
                if(currentPIDSetpoint == -1257) {
                    state = State.MANUAL;
                }
                else {
                    cargoArmPID.setReference(currentPIDSetpoint, ControlType.kPosition, 0,
                        CARGO_ARM_ARB_F * Math.cos(getArmAngle()));
                }
            break;
        }

        speed = 0;

        if (getLimitSwitchPressed()) {
            resetEncoder();
        }
        lastLimit = getLimitSwitch();

        SmartDashboard.putString("Cargo Arm State", state.name());

        SmartDashboard.putNumber("Cargo Arm PID Setpoint", currentPIDSetpoint);
        SmartDashboard.putBoolean("Cargo Arm Limit Switch", getLimitSwitch());
        SmartDashboard.putNumber("Cargo Arm Position", getEncoderPosition());

        SmartDashboard.putNumber("Cargo Arm Current", cargoArmMotor.getOutputCurrent());
        SmartDashboard.putNumber("Cargo Arm Temperature (C)", cargoArmMotor.getMotorTemperature());
    }

    /**
     * Drive the arm with a specific value
     * Scales the given value with a max speed
     *
     * @param value speed of the arm
     */
    public void setSpeed(double value) {
        speed = value * CARGO_ARM_MAX_SPEED;

        // Safety to prevent arm from ripping itself apart
        if(speed < 0 && getLimitSwitch()) {
            speed = 0;
        }

        if(speed != 0) {
            state = State.MANUAL;
        }
    }

    /**
     * Move the arm to the position for scoring in the rocket
     */
    public void moveRocket() {
        setPIDPosition(CARGO_ARM_PID_ROCKET);
    }

    /**
     * Move the arm to the position for scoring in the cargo bay
     */
    public void moveCargo() {
        setPIDPosition(CARGO_ARM_PID_CARGO);
    }

    /**
     * 
     * @param value
     */
    public void freeze() {
        cargoArmPID.setIAccum(0);
        currentPIDSetpoint = getEncoderPosition();
        state = State.FROZEN;
    }

    /**
     * Move the arm to a specific setpoint using PID control
     *
     * @param value position of the arm in motor revolutions
     */
    private void setPIDPosition(double value) {
        cargoArmPID.setIAccum(0);
        currentPIDSetpoint = value;
        state = State.PID;
    }

    /* 
     * Resets the encoder to the bottom position
     */
    private void resetEncoder() {
        cargoArmEncoder.setPosition(0.0);
    }
    
    /* 
     * Resets the encoder to the top position
     */
    private void resetEncoderTop() {
        cargoArmEncoder.setPosition(CARGO_ARM_PID_RAISED);
    }

    /**
     * @return the current velocity of the arm in revolutions
     */
    private double getEncoderPosition() {
        return cargoArmEncoder.getPosition();
    }

    /**
     * @return the current velocity of the arm in revolutions / second
     */
    private double getEncoderVelocity() {
        return cargoArmEncoder.getVelocity();
    }

    /**
     * @return the current angle of the arm in degrees
     */
    private double getArmAngle() {
        return getEncoderPosition() * CARGO_ARM_ANGLE_CONV_FACTOR;
    }

    /**
     * @return whether or not the bottom limit switch is pressed
     */
    private boolean getLimitSwitch() {
        return !limitSwitch.get();
    }

    /**
     * @return whether or not the bottom limit switch has just been pressed
     */
    private boolean getLimitSwitchPressed() {
        return lastLimit != getLimitSwitch();
    }

    /**
     * Set up SmartDashboard/Shuffleboard for constant tuning
     */
    private void setConstantTuning() {
        SmartDashboard.putNumber("Cargo Arm Max Speed", CARGO_ARM_MAX_SPEED);

        SmartDashboard.putNumber("Cargo Arm P", CARGO_ARM_PIDF[0]);
        SmartDashboard.putNumber("Cargo Arm I", CARGO_ARM_PIDF[1]);
        SmartDashboard.putNumber("Cargo Arm D", CARGO_ARM_PIDF[2]);
        SmartDashboard.putNumber("Cargo Arm F", CARGO_ARM_PIDF[3]);
    }

    /**
     * Retrieves constant tuning from SmartDashboard/Shuffleboard
     */
    public void getConstantTuning() {
        CARGO_ARM_MAX_SPEED = SmartDashboard.getNumber("Cargo Arm Max Speed",
                CARGO_ARM_MAX_SPEED);

        if(CARGO_ARM_PIDF[0] != SmartDashboard.getNumber("Cargo Arm P", CARGO_ARM_PIDF[0])) {
            CARGO_ARM_PIDF[0] = SmartDashboard.getNumber("Cargo Arm P", CARGO_ARM_PIDF[0]);
            cargoArmPID.setP(CARGO_ARM_PIDF[0]);
        }
        if(CARGO_ARM_PIDF[1] != SmartDashboard.getNumber("Cargo Arm I", CARGO_ARM_PIDF[1])) {
            CARGO_ARM_PIDF[1] = SmartDashboard.getNumber("Cargo Arm I", CARGO_ARM_PIDF[1]);
            cargoArmPID.setP(CARGO_ARM_PIDF[1]);
        }
        if(CARGO_ARM_PIDF[2] != SmartDashboard.getNumber("Cargo Arm D", CARGO_ARM_PIDF[2])) {
            CARGO_ARM_PIDF[2] = SmartDashboard.getNumber("Cargo Arm D", CARGO_ARM_PIDF[2]);
            cargoArmPID.setP(CARGO_ARM_PIDF[2]);
        }
        if(CARGO_ARM_PIDF[3] != SmartDashboard.getNumber("Cargo Arm F", CARGO_ARM_PIDF[3])) {
            CARGO_ARM_PIDF[3] = SmartDashboard.getNumber("Cargo Arm F", CARGO_ARM_PIDF[3]);
            cargoArmPID.setP(CARGO_ARM_PIDF[3]);
        }
    }

    public State getState() {
        return state;
    }
}

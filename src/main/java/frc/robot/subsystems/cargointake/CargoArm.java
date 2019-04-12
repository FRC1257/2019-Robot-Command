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
    
    public enum State {
        MANUAL, PID
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
        }

        speed = 0;

        if (getLimitSwitchPressed()) {
            resetEncoder();
        }
        lastLimit = getLimitSwitch();

        SmartDashboard.putNumber("Intake Arm PID Setpoint", currentPIDSetpoint);
        SmartDashboard.putBoolean("Intake Arm Limit Switch", getLimitSwitch());
        SmartDashboard.putNumber("Intake Arm Position", getEncoderPosition());

        SmartDashboard.putNumber("Intake Arm Current", cargoArmMotor.getOutputCurrent());
        SmartDashboard.putNumber("Intake Arm Temperature (C)", cargoArmMotor.getMotorTemperature());
    }

    public void setSpeed(double value) {
        speed = value * CARGO_ARM_MAX_SPEED;

        if(speed != 0) {
            state = State.MANUAL;
        }
    }

    public void moveRocket() {
        setPIDPosition(CARGO_ARM_PID_ROCKET);
    }

    public void moveCargo() {
        setPIDPosition(CARGO_ARM_PID_CARGO);
    }

    private void setPIDPosition(double value) {
        cargoArmPID.setIAccum(0);
        currentPIDSetpoint = value;
        state = State.PID;
    }

    /* 
     * Resets the encoder to the bottom position
     */
    public void resetEncoder() {
        cargoArmEncoder.setPosition(0.0);
    }
    
    /* 
     * Resets the encoder to the top position
     */
    public void resetEncoderTop() {
        cargoArmEncoder.setPosition(CARGO_ARM_PID_RAISED);
    }

    public double getEncoderPosition() {
        return cargoArmEncoder.getPosition();
    }

    public double getEncoderVelocity() {
        return cargoArmEncoder.getVelocity();
    }

    /**
     * Returns the current angle of the arm in degrees
     */
    public double getArmAngle() {
        return getEncoderPosition() * CARGO_ARM_ANGLE_CONV_FACTOR;
    }

    // Whether or not the bottom limit switch is pressed
    public boolean getLimitSwitch() {
        return !limitSwitch.get();
    }

    // Whether or not the bottom limit switch is pressed
    public boolean getLimitSwitchPressed() {
        return lastLimit != getLimitSwitch();
    }
    
    private void setConstantTuning() {
        SmartDashboard.putNumber("Intake Arm Max Speed", CARGO_ARM_MAX_SPEED);

        SmartDashboard.putNumber("Intake Arm P", CARGO_ARM_PIDF[0]);
        SmartDashboard.putNumber("Intake Arm I", CARGO_ARM_PIDF[1]);
        SmartDashboard.putNumber("Intake Arm D", CARGO_ARM_PIDF[2]);
        SmartDashboard.putNumber("Intake Arm F", CARGO_ARM_PIDF[3]);
    }

    public void getConstantTuning() {
        CARGO_ARM_MAX_SPEED = SmartDashboard.getNumber("Intake Arm Max Speed",
                CARGO_ARM_MAX_SPEED);

        if(CARGO_ARM_PIDF[0] != SmartDashboard.getNumber("Intake Arm P", CARGO_ARM_PIDF[0])) {
            CARGO_ARM_PIDF[0] = SmartDashboard.getNumber("Intake Arm P", CARGO_ARM_PIDF[0]);
            cargoArmPID.setP(CARGO_ARM_PIDF[0]);
        }
        if(CARGO_ARM_PIDF[1] != SmartDashboard.getNumber("Intake Arm I", CARGO_ARM_PIDF[1])) {
            CARGO_ARM_PIDF[1] = SmartDashboard.getNumber("Intake Arm I", CARGO_ARM_PIDF[1]);
            cargoArmPID.setP(CARGO_ARM_PIDF[1]);
        }
        if(CARGO_ARM_PIDF[2] != SmartDashboard.getNumber("Intake Arm D", CARGO_ARM_PIDF[2])) {
            CARGO_ARM_PIDF[2] = SmartDashboard.getNumber("Intake Arm D", CARGO_ARM_PIDF[2]);
            cargoArmPID.setP(CARGO_ARM_PIDF[2]);
        }
        if(CARGO_ARM_PIDF[3] != SmartDashboard.getNumber("Intake Arm F", CARGO_ARM_PIDF[3])) {
            CARGO_ARM_PIDF[3] = SmartDashboard.getNumber("Intake Arm F", CARGO_ARM_PIDF[3]);
            cargoArmPID.setP(CARGO_ARM_PIDF[3]);
        }
    }

    public State getState() {
        return state;
    }
}

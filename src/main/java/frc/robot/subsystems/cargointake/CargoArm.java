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

    private CANSparkMax cargoArmMotor;
    private CANEncoder cargoArmEncoder;
    private CANPIDController cargoArmPID;

    private DigitalInput limitSwitch;
    private boolean lastLimit;

    private double speed;
    private double currentPIDSetpoint;
    
    public static enum State {
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
        cargoArmPID.setP(RobotMap.CARGO_ARM_PIDF[0]);
        cargoArmPID.setI(RobotMap.CARGO_ARM_PIDF[1]);
        cargoArmPID.setD(RobotMap.CARGO_ARM_PIDF[2]);
        cargoArmPID.setFF(RobotMap.CARGO_ARM_PIDF[3]);
        cargoArmPID.setIZone(0.0);
        cargoArmPID.setOutputRange(RobotMap.CARGO_ARM_PID_MIN_OUTPUT, RobotMap.CARGO_ARM_PID_MAX_OUTPUT);
        
        limitSwitch = new DigitalInput(RobotMap.CARGO_ARM_LIMIT_SWITCH_ID);
        lastLimit = getLimitSwitch();

        setConstantTuning();
        reset();
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new CargoArmCommand());
    }
    
    public void reset() {
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
                        RobotMap.CARGO_ARM_ARB_F * Math.cos(getArmAngle()));

                    double error = Math.abs(getEncoderPosition() - currentPIDSetpoint);
                    if(error < RobotMap.CARGO_ARM_PID_TOLERANCE) {
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
        SmartDashboard.putNumber("Intake Arm Velocity", getEncoderVelocity());

        SmartDashboard.putNumber("Intake Arm Current", cargoArmMotor.getOutputCurrent());
        SmartDashboard.putNumber("Intake Arm Temperature (C)", cargoArmMotor.getMotorTemperature());
    }

    public void setSpeed(double value) {
        speed = value * RobotMap.CARGO_ARM_MAX_SPEED;

        if(speed != 0) {
            state = State.MANUAL;
        }
    }

    public void moveRocket() {
        setPIDPosition(RobotMap.CARGO_ARM_PID_ROCKET);
    }

    public void moveCargo() {
        setPIDPosition(RobotMap.CARGO_ARM_PID_CARGO);
    }

    public void setPIDPosition(double value) {
        cargoArmPID.setIAccum(0);
        currentPIDSetpoint = value;
        state = State.PID;
    }

    public void endPID() {
        state = State.MANUAL;
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
        cargoArmEncoder.setPosition(RobotMap.CARGO_ARM_PID_RAISED);
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
        return getEncoderPosition() * RobotMap.CARGO_ARM_ANGLE_CONV_FACTOR;
    }

    // Whether or not the bottom limit switch is pressed
    public boolean getLimitSwitch() {
        return !limitSwitch.get();
    }

    // Whether or not the bottom limit switch is pressed
    public boolean getLimitSwitchPressed() {
        return lastLimit != getLimitSwitch();
    }
    
    public void setConstantTuning() {
        SmartDashboard.putNumber("Intake Arm Max Speed", RobotMap.CARGO_ARM_MAX_SPEED);

        SmartDashboard.putNumber("Intake Arm P", RobotMap.CARGO_ARM_PIDF[0]);
        SmartDashboard.putNumber("Intake Arm I", RobotMap.CARGO_ARM_PIDF[1]);
        SmartDashboard.putNumber("Intake Arm D", RobotMap.CARGO_ARM_PIDF[2]);
        SmartDashboard.putNumber("Intake Arm F", RobotMap.CARGO_ARM_PIDF[3]);
    }

    public void getConstantTuning() {
        RobotMap.CARGO_ARM_MAX_SPEED = SmartDashboard.getNumber("Intake Arm Max Speed",
                RobotMap.CARGO_ARM_MAX_SPEED);

        if(RobotMap.CARGO_ARM_PIDF[0] != SmartDashboard.getNumber("Intake Arm P", RobotMap.CARGO_ARM_PIDF[0])) {
            RobotMap.CARGO_ARM_PIDF[0] = SmartDashboard.getNumber("Intake Arm P", RobotMap.CARGO_ARM_PIDF[0]);
            cargoArmPID.setP(RobotMap.CARGO_ARM_PIDF[0]);
        }
        if(RobotMap.CARGO_ARM_PIDF[1] != SmartDashboard.getNumber("Intake Arm I", RobotMap.CARGO_ARM_PIDF[1])) {
            RobotMap.CARGO_ARM_PIDF[1] = SmartDashboard.getNumber("Intake Arm I", RobotMap.CARGO_ARM_PIDF[1]);
            cargoArmPID.setP(RobotMap.CARGO_ARM_PIDF[1]);
        }
        if(RobotMap.CARGO_ARM_PIDF[2] != SmartDashboard.getNumber("Intake Arm D", RobotMap.CARGO_ARM_PIDF[2])) {
            RobotMap.CARGO_ARM_PIDF[2] = SmartDashboard.getNumber("Intake Arm D", RobotMap.CARGO_ARM_PIDF[2]);
            cargoArmPID.setP(RobotMap.CARGO_ARM_PIDF[2]);
        }
        if(RobotMap.CARGO_ARM_PIDF[3] != SmartDashboard.getNumber("Intake Arm F", RobotMap.CARGO_ARM_PIDF[3])) {
            RobotMap.CARGO_ARM_PIDF[3] = SmartDashboard.getNumber("Intake Arm F", RobotMap.CARGO_ARM_PIDF[3]);
            cargoArmPID.setP(RobotMap.CARGO_ARM_PIDF[3]);
        }
    }

    public State getState() {
        return state;
    }
}

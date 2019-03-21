package frc.robot.subsystems;

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
import frc.robot.commands.intakearm.*;

public class IntakeArm extends Subsystem {

    private static IntakeArm instance = null;

    private CANSparkMax intakeArmMotor;
    private CANEncoder intakeArmEncoder;
    private CANPIDController intakeArmPID;

    private DigitalInput limitSwitch;
    private boolean lastLimit;

    private double speed;
    private double currentPIDSetpoint;
    
    public static enum State {
        MANUAL, PID
    }
    private State state;

    private IntakeArm() {
        intakeArmMotor = new CANSparkMax(RobotMap.INTAKE_ARM_MOTOR_ID, MotorType.kBrushless);
        intakeArmMotor.restoreFactoryDefaults();
        intakeArmMotor.setIdleMode(IdleMode.kBrake);
        intakeArmMotor.setSmartCurrentLimit(RobotMap.NEO_CURRENT_LIMIT);
        intakeArmEncoder = intakeArmMotor.getEncoder();
        intakeArmPID = intakeArmMotor.getPIDController();
        intakeArmPID.setP(RobotMap.INTAKE_ARM_PIDF[0]);
        intakeArmPID.setI(RobotMap.INTAKE_ARM_PIDF[1]);
        intakeArmPID.setD(RobotMap.INTAKE_ARM_PIDF[2]);
        intakeArmPID.setFF(RobotMap.INTAKE_ARM_PIDF[3]);
        intakeArmPID.setIZone(0.0);
        intakeArmPID.setOutputRange(RobotMap.INTAKE_ARM_PID_MIN_OUTPUT, RobotMap.INTAKE_ARM_PID_MAX_OUTPUT);
        
        limitSwitch = new DigitalInput(RobotMap.INTAKE_ARM_LIMIT_SWITCH_ID);
        lastLimit = getLimitSwitch();

        setConstantTuning();
        reset();
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new IntakeArmCommand());
    }
    
    public void reset() {
        intakeArmMotor.set(0);

        currentPIDSetpoint = -1257;
        resetEncoderTop();

        state = State.MANUAL;
    }

    public void update() {
        switch(state) {
            case MANUAL:
                intakeArmMotor.set(speed);
                currentPIDSetpoint = -1257;
            break;
            case PID:
                if(currentPIDSetpoint == -1257) {
                    state = State.MANUAL;
                }
                else {
                    intakeArmPID.setReference(currentPIDSetpoint, ControlType.kPosition);

                    double error = Math.abs(getEncoderPosition() - currentPIDSetpoint);
                    if(error < RobotMap.INTAKE_ARM_TOLERANCE) {
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

        SmartDashboard.putNumber("Intake Arm Current", intakeArmMotor.getOutputCurrent());
        SmartDashboard.putNumber("Intake Arm Temperature (C)", intakeArmMotor.getMotorTemperature());
    }

    public void setSpeed(double value) {
        speed = value * RobotMap.INTAKE_ARM_MOTOR_MAX_SPEED;

        if(speed != 0) {
            state = State.MANUAL;
        }
    }

    public void moveRocket() {
        setPIDPosition(RobotMap.INTAKE_ARM_PID_ROCKET);
    }

    public void moveCargo() {
        setPIDPosition(RobotMap.INTAKE_ARM_PID_CARGO);
    }

    public void setPIDPosition(double value) {
        intakeArmPID.setIAccum(0);
        currentPIDSetpoint = value;
        state = State.PID;
    }

    /* 
     * Resets the encoder to the bottom position
     */
    public void resetEncoder() {
        intakeArmEncoder.setPosition(0.0);
    }
    
    /* 
     * Resets the encoder to the top position
     */
    public void resetEncoderTop() {
        intakeArmEncoder.setPosition(RobotMap.INTAKE_ARM_PID_RAISED);
    }

    public double getEncoderPosition() {
        
        return intakeArmEncoder.getPosition();
    }

    public double getEncoderVelocity() {
        return intakeArmEncoder.getVelocity();
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
        SmartDashboard.putNumber("Intake Arm Max Speed", RobotMap.INTAKE_ARM_MOTOR_MAX_SPEED);

        SmartDashboard.putNumber("Intake Arm P", RobotMap.INTAKE_ARM_PIDF[0]);
        SmartDashboard.putNumber("Intake Arm I", RobotMap.INTAKE_ARM_PIDF[1]);
        SmartDashboard.putNumber("Intake Arm D", RobotMap.INTAKE_ARM_PIDF[2]);
        SmartDashboard.putNumber("Intake Arm F", RobotMap.INTAKE_ARM_PIDF[3]);
    }

    public void getConstantTuning() {
        RobotMap.INTAKE_ARM_MOTOR_MAX_SPEED = SmartDashboard.getNumber("Intake Arm Max Speed",
                RobotMap.INTAKE_ARM_MOTOR_MAX_SPEED);

        if(RobotMap.INTAKE_ARM_PIDF[0] != SmartDashboard.getNumber("Intake Arm P", RobotMap.INTAKE_ARM_PIDF[0])) {
            RobotMap.INTAKE_ARM_PIDF[0] = SmartDashboard.getNumber("Intake Arm P", RobotMap.INTAKE_ARM_PIDF[0]);
            intakeArmPID.setP(RobotMap.INTAKE_ARM_PIDF[0]);
        }
        if(RobotMap.INTAKE_ARM_PIDF[1] != SmartDashboard.getNumber("Intake Arm I", RobotMap.INTAKE_ARM_PIDF[1])) {
            RobotMap.INTAKE_ARM_PIDF[1] = SmartDashboard.getNumber("Intake Arm I", RobotMap.INTAKE_ARM_PIDF[1]);
            intakeArmPID.setP(RobotMap.INTAKE_ARM_PIDF[1]);
        }
        if(RobotMap.INTAKE_ARM_PIDF[2] != SmartDashboard.getNumber("Intake Arm D", RobotMap.INTAKE_ARM_PIDF[2])) {
            RobotMap.INTAKE_ARM_PIDF[2] = SmartDashboard.getNumber("Intake Arm D", RobotMap.INTAKE_ARM_PIDF[2]);
            intakeArmPID.setP(RobotMap.INTAKE_ARM_PIDF[2]);
        }
        if(RobotMap.INTAKE_ARM_PIDF[3] != SmartDashboard.getNumber("Intake Arm F", RobotMap.INTAKE_ARM_PIDF[3])) {
            RobotMap.INTAKE_ARM_PIDF[3] = SmartDashboard.getNumber("Intake Arm F", RobotMap.INTAKE_ARM_PIDF[3]);
            intakeArmPID.setP(RobotMap.INTAKE_ARM_PIDF[3]);
        }
    }

    public State getState() {
        return state;
    }

    public static IntakeArm getInstance() {
        if (instance == null) {
            instance = new IntakeArm();
        }
        return instance;
    }
}

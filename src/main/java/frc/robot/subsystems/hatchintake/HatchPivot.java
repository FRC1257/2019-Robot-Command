package frc.robot.subsystems.hatchintake;

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
import frc.robot.commands.hatchintake.hatchpivot.*;

public class HatchPivot extends Subsystem {

    private static HatchPivot instance = null;

    private CANSparkMax hatchPivotMotor;
    private CANEncoder hatchPivotEncoder;
    private CANPIDController hatchPivotPID;

    private DigitalInput limitSwitch;
    private boolean lastLimit;

    private double speed;
    private double currentPIDSetpoint;
    
    public static enum State {
        MANUAL, PID
    }
    private State state;

    private HatchPivot() {
        hatchPivotMotor = new CANSparkMax(RobotMap.HATCH_PIVOT_MOTOR_ID, MotorType.kBrushless);
        hatchPivotMotor.setIdleMode(IdleMode.kBrake);
        hatchPivotMotor.setSmartCurrentLimit(RobotMap.NEO_CURRENT_LIMIT);
        hatchPivotEncoder = hatchPivotMotor.getEncoder();
        hatchPivotPID = hatchPivotMotor.getPIDController();
        hatchPivotPID.setP(RobotMap.HATCH_PIDF[0]);
        hatchPivotPID.setI(RobotMap.HATCH_PIDF[1]);
        hatchPivotPID.setD(RobotMap.HATCH_PIDF[2]);
        hatchPivotPID.setFF(RobotMap.HATCH_PIDF[3]);
        hatchPivotPID.setIZone(0.0);
        hatchPivotPID.setOutputRange(RobotMap.HATCH_PID_MIN_OUTPUT, RobotMap.HATCH_PID_MAX_OUTPUT);

        limitSwitch = new DigitalInput(RobotMap.HATCH_LIMIT_SWITCH_PIVOT_ID);
        lastLimit = true;

        setConstantTuning();
        reset();
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new HatchPivotCommand());
    }
    
    public void reset() {
        hatchPivotMotor.set(0);

        currentPIDSetpoint = -1257;
        resetEncoder();

        state = State.MANUAL;
    }

    public void update() {
        switch(state) {
            case MANUAL:
                hatchPivotMotor.set(speed);
                currentPIDSetpoint = -1257;
            break;
            case PID:
                if(currentPIDSetpoint == -1257) {
                    state = State.MANUAL;
                }
                else {
                    hatchPivotPID.setReference(currentPIDSetpoint, ControlType.kPosition);

                    double error = Math.abs(getEncoderPosition() - currentPIDSetpoint);
                    if(error < RobotMap.HATCH_TOLERANCE) {
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

        SmartDashboard.putNumber("Hatch PID Setpoint", currentPIDSetpoint);
        SmartDashboard.putBoolean("Hatch Pivot Limit Switch", getLimitSwitch());
        SmartDashboard.putNumber("Hatch Position", getEncoderPosition());
        SmartDashboard.putNumber("Hatch Velocity", getEncoderVelocity());

        SmartDashboard.putNumber("Hatch Current", hatchPivotMotor.getOutputCurrent());
        SmartDashboard.putNumber("Hatch Temperature (C)", hatchPivotMotor.getMotorTemperature());
    }

    public void setSpeed(double value) {
        speed = value * RobotMap.HATCH_MOTOR_MAX_SPEED;

        if(speed != 0) {
            state = State.MANUAL;
        }
    }

    public void raisePivot() {
        setPIDPosition(RobotMap.HATCH_PID_RAISED);
    }

    public void lowerPivot() {
        setPIDPosition(RobotMap.HATCH_PID_LOWERED);
    }

    public void setPIDPosition(double value) {
        hatchPivotPID.setIAccum(0);
        currentPIDSetpoint = value;
        state = State.PID;
    }

    /* 
     * Resets the encoder to the bottom position
     */
    public void resetEncoder() {
        hatchPivotEncoder.setPosition(0.0);
    }

    public double getEncoderPosition() {
        return hatchPivotEncoder.getPosition();
    }

    public double getEncoderVelocity() {
        return hatchPivotEncoder.getVelocity();
    }

    public boolean getLimitSwitch() {
        return !limitSwitch.get();
    }

    // Returns when the limit switch is first pressed or first released
    public boolean getLimitSwitchPressed() {
        return lastLimit != getLimitSwitch();
    }
    
    public void setConstantTuning() {
        SmartDashboard.putNumber("Hatch Max Speed", RobotMap.HATCH_MOTOR_MAX_SPEED);

        SmartDashboard.putNumber("Hatch P", RobotMap.HATCH_PIDF[0]);
        SmartDashboard.putNumber("Hatch I", RobotMap.HATCH_PIDF[1]);
        SmartDashboard.putNumber("Hatch D", RobotMap.HATCH_PIDF[2]);
        SmartDashboard.putNumber("Hatch F", RobotMap.HATCH_PIDF[3]);
    }

    public void getConstantTuning() {
        RobotMap.HATCH_MOTOR_MAX_SPEED = SmartDashboard.getNumber("Hatch Max Speed", RobotMap.HATCH_MOTOR_MAX_SPEED);
        
        if(RobotMap.HATCH_PIDF[0] != SmartDashboard.getNumber("Hatch P", RobotMap.HATCH_PIDF[0])) {
            RobotMap.HATCH_PIDF[0] = SmartDashboard.getNumber("Hatch P", RobotMap.HATCH_PIDF[0]);
            hatchPivotPID.setP(RobotMap.HATCH_PIDF[0]);
        }
        if(RobotMap.HATCH_PIDF[1] != SmartDashboard.getNumber("Hatch I", RobotMap.HATCH_PIDF[1])) {
            RobotMap.HATCH_PIDF[1] = SmartDashboard.getNumber("Hatch I", RobotMap.HATCH_PIDF[1]);
            hatchPivotPID.setP(RobotMap.HATCH_PIDF[1]);
        }
        if(RobotMap.HATCH_PIDF[2] != SmartDashboard.getNumber("Hatch D", RobotMap.HATCH_PIDF[2])) {
            RobotMap.HATCH_PIDF[2] = SmartDashboard.getNumber("Hatch D", RobotMap.HATCH_PIDF[2]);
            hatchPivotPID.setP(RobotMap.HATCH_PIDF[2]);
        }
        if(RobotMap.HATCH_PIDF[3] != SmartDashboard.getNumber("Hatch F", RobotMap.HATCH_PIDF[3])) {
            RobotMap.HATCH_PIDF[3] = SmartDashboard.getNumber("Hatch F", RobotMap.HATCH_PIDF[3]);
            hatchPivotPID.setP(RobotMap.HATCH_PIDF[3]);
        }
    }

    public State getState() {
        return state;
    }

    public static HatchPivot getInstance() {
        if (instance == null) {
            instance = new HatchPivot();
        }
        return instance;
    }
}

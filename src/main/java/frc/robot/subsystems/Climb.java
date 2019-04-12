package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import frc.robot.Robot;
import frc.robot.RobotMap;

public class Climb extends Subsystem {

    // Constants
    public static double CLIMB_DRIVE_MAX_SPEED = 1.0; // percentage
    public static final double CLIMB_CRITICAL_ANGLE = 5.0; // Critical angle before the climb stabilizer kicks in (degrees)

    private DoubleSolenoid frontSolenoid;
    private DoubleSolenoid backSolenoid;

    private WPI_VictorSPX frontMotor;
    private WPI_VictorSPX backMotor;

    private double frontSpeed;
    private double backSpeed;

    public enum State {
        GROUND, EXTENDED, HALF, SECONDARY_RAISE, SECONDARY_FREEZE, MANUAL
    }
    private State state = State.GROUND;

    public Climb() {
        frontSolenoid = new DoubleSolenoid(RobotMap.PCM_SECONDARY_ID, 
            RobotMap.CLIMB_FRONT_SOLENOID_FORWARD_ID, 
            RobotMap.CLIMB_FRONT_SOLENOID_REVERSE_ID);
        backSolenoid = new DoubleSolenoid(RobotMap.PCM_SECONDARY_ID, 
            RobotMap.CLIMB_BACK_SOLENOID_FORWARD_ID, 
            RobotMap.CLIMB_BACK_SOLENOID_REVERSE_ID);

        frontMotor = new WPI_VictorSPX(RobotMap.CLIMB_FRONT_MOTOR_ID);
        backMotor = new WPI_VictorSPX(RobotMap.CLIMB_BACK_MOTOR_ID);
        frontMotor.setNeutralMode(NeutralMode.Brake);
        backMotor.setNeutralMode(NeutralMode.Brake);

        setConstantTuning();
        reset();
    }

    @Override
    public void initDefaultCommand() {
        // No default command
    }
    
    public void reset() {   
        frontMotor.set(0.0);
        backMotor.set(0.0);

        frontSpeed = 0.0;
        backSpeed = 0.0;
        
        retractFront();
        retractBack();

        Robot.gyro.zeroClimbTiltAngle();
        state = State.GROUND;
    }

    public void update() {
        switch(state) {
            case GROUND:
                retractFront();
                retractBack();
            break;
            case EXTENDED: // Climb Stabilizer
                double angle = Robot.gyro.getClimbTiltAngle();
                // Robot is tilted forwards, so stop back
                if(angle > CLIMB_CRITICAL_ANGLE) {
                    turnOffBack();
                }
                // Robot is tilted backwards, so stop front
                else if(angle < -CLIMB_CRITICAL_ANGLE) {
                    turnOffFront();
                }
                // Otherwise, just extend both
                else {
                    extendFront();
                    extendBack();
                }
            break;
            case HALF:
                extendFront();
                retractBack();
            break;
            case SECONDARY_RAISE:
                extendBack();
            break;
            case SECONDARY_FREEZE:
                turnOffBack();
            break;
            case MANUAL:
                // Once this state is reached, the climb will only be actuated through manual procedures until reset
                break;
        }

        climbDrive(Robot.oi.getClimbDriveSpeed());

        frontMotor.set(frontSpeed);
        backMotor.set(backSpeed);
        frontSpeed = 0.0;
        backSpeed = 0.0;

        SmartDashboard.putBoolean("Climb Front Extended", isFrontExtended());
        SmartDashboard.putBoolean("Climb Back Extended", isBackExtended());
        SmartDashboard.putString("Climb State", state.name());
    }

    public void toggleFront() {
        if(isFrontExtended()) retractFront();
        else extendFront();

        state = State.MANUAL;
    }

    private void retractFront() {
        frontSolenoid.set(Value.kForward);
    }

    private void extendFront() {
        frontSolenoid.set(Value.kReverse); 
    }

    private void turnOffFront() {
        frontSolenoid.set(Value.kOff);
    }

    public void toggleBack() {
        if(isBackExtended()) retractBack();
        else extendBack();

        state = State.MANUAL;
    }

    private void retractBack() {
        backSolenoid.set(Value.kForward);
    }

    private void extendBack() {
        backSolenoid.set(Value.kReverse);
    }

    private void turnOffBack() {
        backSolenoid.set(Value.kOff);
    }

    /** 
     *  Go to the next state of the climb
     */
    public void advanceClimb() {
        switch(state) {
            case GROUND:
                Robot.gyro.zeroClimbTiltAngle();
                state = State.EXTENDED;
            break;
            case EXTENDED:
                state = State.HALF;
            break;
            case HALF:
                state = State.GROUND;
            break;
            case SECONDARY_RAISE:
                // Intentionally Empty
            break;
            case SECONDARY_FREEZE:
                // Intentionally Empty
            break;
            case MANUAL:
                // Intentionally Empty
            break;
        }
    }

    /** 
     *  Go to the prev state of the climb
     */
    public void backClimb() {
        switch(state) {
            case GROUND:
                state = State.HALF;
            break;
            case EXTENDED:
                state = State.GROUND;
            break;
            case HALF:
                state = State.EXTENDED;
            break;
            case SECONDARY_RAISE:
                // Intentionally Empty
            break;
            case SECONDARY_FREEZE:
                // Intentionally Empty
            break;
            case MANUAL:
                // Intentionally Empty
            break;
        }
    }

    /** 
     * Advance our level 2 climb
     */
    public void advanceSecondaryClimb() {
        switch(state) {
            case GROUND:
                state = State.SECONDARY_RAISE;
            break;
            case EXTENDED:
                // Intentionally Empty
            break;
            case HALF:
                // Intentionally Empty
            break;
            case SECONDARY_RAISE:
                state = State.SECONDARY_FREEZE;
            break;
            case SECONDARY_FREEZE:
                state = State.GROUND;
            break;
            case MANUAL:
                // Intentionally Empty
            break;
        }
    }

    private void climbDrive(double speed) {
        double adjustedSpeed = speed * CLIMB_DRIVE_MAX_SPEED;
        frontSpeed = adjustedSpeed;
        backSpeed = adjustedSpeed;
    }

    // Whether or not the front is currently extended
    private boolean isFrontExtended() {
        return frontSolenoid.get() == DoubleSolenoid.Value.kReverse;
    }

    // Whether or not the back is currently extended
    private boolean isBackExtended() {
        return backSolenoid.get() == DoubleSolenoid.Value.kReverse;
    }

    private void setConstantTuning() {
        
    }

    public void getConstantTuning() {
        
    }

    public State getState() {
        return state;
    }
}

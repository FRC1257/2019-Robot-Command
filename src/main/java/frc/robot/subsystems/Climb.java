package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.util.Gyro;

/**
 * Subsystem to handle our hab climb mechanism
 *  - Utilizes 3 position double solenoids to control front and back pistons to rise up
 *  - Utilizes 2 motors to control a sub-drivetrain at the bottom of the pistons
 *  - Ability to do a Level 3 and Level 2 climb
 *  - Uses the gyro to correct for robot tilt during the climb
 */
public class Climb extends Subsystem {

    // Constants
    public static double CLIMB_DRIVE_MAX_SPEED = 1.0; // percentage
    public static final double CLIMB_CRITICAL_ANGLE = 5.0; // Critical angle before the climb stabilizer kicks in (degrees)

    private DoubleSolenoid frontSolenoid;
    private DoubleSolenoid backSolenoid;

    private WPI_VictorSPX frontMotor;
    private WPI_VictorSPX backMotor;

    private Value frontState;
    private Value backState;

    private double frontSpeed;
    private double backSpeed;

    /**
     * GROUND - neutral state of the climb, both front and back retracted
     * EXTENDED - first stage of level 3, both front and back extended
     * HALF - second stage of level 3, front extended, back retracted
     * SECONDARY_RAISE - first stage of level 2, back extended
     * SECONDARY_FREEZE - second stage of level 2, back frozen
     * MANUAL - front/back manually actuated for testing purposes
     */
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
        retractFront();
        retractBack();

        frontMotor.set(0.0);
        backMotor.set(0.0);

        frontSpeed = 0.0;
        backSpeed = 0.0;

        Gyro.getInstance().zeroClimbTiltAngle();
        state = State.GROUND;
    }

    /**
     * Update motor/solenoid outputs according to the current state
     */
    public void update() {
        switch(state) {
            case GROUND:
                retractFront();
                retractBack();
            break;
            case EXTENDED: // Climb Stabilizer
                double angle = Gyro.getInstance().getClimbTiltAngle();
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

        frontSolenoid.set(frontState);
        backSolenoid.set(backState);

        frontMotor.set(frontSpeed);
        backMotor.set(backSpeed);
        frontSpeed = 0.0;
        backSpeed = 0.0;

        SmartDashboard.putBoolean("Climb Front Extended", isFrontExtended());
        SmartDashboard.putBoolean("Climb Back Extended", isBackExtended());
        SmartDashboard.putString("Climb State", state.name());
    }

    /**
     * Toggle the state of the front solenoid
     * Using this function sends the climb into the manual testing state
     */
    public void toggleFront() {
        if(isFrontExtended()) retractFront();
        else extendFront();

        state = State.MANUAL;
    }

    /**
     * Retract the front solenoid
     */
    private void retractFront() {
        frontState = Value.kForward;
    }

    /**
     * extend the front solenoid
     */
    private void extendFront() {
        frontState = Value.kReverse;
    }

    /**
     * Freeze the front solenoid
     */
    private void turnOffFront() {
        frontState = Value.kOff;
    }

    /**
     * Toggle the back solenoid
     * Using this function sends the climb into the manual testing state
     */
    public void toggleBack() {
        if(isBackExtended()) retractBack();
        else extendBack();

        state = State.MANUAL;
    }

    /**
     * Retract the back solenoid
     */
    private void retractBack() {
        backState = Value.kForward;
    }

    /**
     * Extend the back solenoid
     */
    private void extendBack() {
        backState = Value.kReverse;
    }

    /**
     * Freeze the back solenoid
     */
    private void turnOffBack() {
        backState = Value.kOff;
    }

    /** 
     *  Go to the next state of the climb
     */
    public void advanceClimb() {
        switch(state) {
            case GROUND:
                Gyro.getInstance().zeroClimbTiltAngle();
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

    /**
     * Update the speed of the sub-drivetrain
     *
     * @param speed speed of the sub-drivetrain
     */
    private void climbDrive(double speed) {
        double adjustedSpeed = speed * CLIMB_DRIVE_MAX_SPEED;
        frontSpeed = adjustedSpeed;
        backSpeed = adjustedSpeed;
    }


    /**
     * @return whether or not the front is currently extended
     */
    private boolean isFrontExtended() {
        return frontState == DoubleSolenoid.Value.kReverse;
    }

    /**
     * @return whether or not the back is currently extended
     */
    private boolean isBackExtended() {
        return backState == DoubleSolenoid.Value.kReverse;
    }

    /**
     * Set up SmartDashboard/Shuffleboard for constant tuning
     */
    private void setConstantTuning() {
        
    }

    /**
     * Retrieves constant tuning from SmartDashboard/Shuffleboard
     */
    public void getConstantTuning() {
        
    }

    public State getState() {
        return state;
    }
}

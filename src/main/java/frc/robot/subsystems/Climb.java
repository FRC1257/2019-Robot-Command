package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import frc.robot.Robot;
import frc.robot.RobotMap;

/**
 * Subsystem to handle our Level 3 and Level 2 Climb
 *  - Uses 2 3-Position Double Solenoids for lifting the front and back
 *  - Uses 2 VictorSPXs for a sub-drivetrain while climbing
 *  - Calculates the tilt of the robot to correct for the climb
 */

public class Climb extends Subsystem {

    private DoubleSolenoid frontSolenoid;
    private DoubleSolenoid backSolenoid;

    private WPI_VictorSPX frontMotor;
    private WPI_VictorSPX backMotor;

    private double frontSpeed;
    private double backSpeed;

    /**
     * GROUND - Default state with neither solenoid extended (Front - Hatch, Back - Cargo)
     * MANUAL - Manual testing mode for climb. Should never be used in a match
     * 
     * Level 3 States:
     *  - EXTENDED - Both solenoids are extending (uses a climb stabilizer during this step)
     *  - HALF - Retracts the back solenoid to get onto Level 3
     * 
     * Level 2 States:
     *  - SECONDARY_RAISE - Begins extending the back solenoid to level the robot
     *  - SECONDARY_FREEZE - Freezes the back solenoid to allow driving onto Level 2
     */
    public static enum State {
        GROUND, EXTENDED, HALF, SECONDARY_RAISE, SECONDARY_FREEZE, MANUAL
    }
    private State state = State.GROUND;

    public Climb() {
        frontSolenoid = new DoubleSolenoid(RobotMap.PCM_SECONDARY_ID, RobotMap.CLIMB_FRONT_SOLENOID_FORWARD_ID, RobotMap.CLIMB_FRONT_SOLENOID_REVERSE_ID);
        backSolenoid = new DoubleSolenoid(RobotMap.PCM_SECONDARY_ID, RobotMap.CLIMB_BACK_SOLENOID_FORWARD_ID, RobotMap.CLIMB_BACK_SOLENOID_REVERSE_ID);

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

    /**
     * Update the state of the solenoid depending on climb state
     * Send motor output for the sub-drivetrain
     * Output debugging values
     */
    public void update() {
        switch(state) {
            case GROUND:
                retractFront();
                retractBack();
            break;
            case EXTENDED: // Climb Stabilizer
                double angle = Robot.gyro.getClimbTiltAngle();
                // Robot is tilted forwards, so stop back
                if(angle > RobotMap.CLIMB_CRITICAL_ANGLE) {
                    turnOffBack();
                }
                // Robot is tilted backwards, so stop front
                else if(angle < -RobotMap.CLIMB_CRITICAL_ANGLE) {
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
                retractFront();
            break;
            case SECONDARY_FREEZE:
                turnOffBack();
                retractFront();
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
        SmartDashboard.putNumber("Climb Front Speed", frontMotor.get());
        SmartDashboard.putNumber("Climb Back Speed", backMotor.get());
        SmartDashboard.putString("Climb State", state.name());
    }

    /**
     * Toggles the state of the hatch side solenoid
     */
    public void toggleFront() {
        if(isFrontExtended()) retractFront();
        else extendFront();

        state = State.MANUAL;
    }

    /**
     * Retracts the hatch side solenoid
     */
    public void retractFront() {
        frontSolenoid.set(Value.kForward);
    }

    /**
     * Extends the hatch side solenoid
     */
    public void extendFront() {
        frontSolenoid.set(Value.kReverse); 
    }

    /**
     * Freezes the hatch side solenoid
     */
    public void turnOffFront() {
        frontSolenoid.set(Value.kOff);
    }

    /**
     * Toggles the state of the cargo side solenoid
     */
    public void toggleBack() {
        if(isBackExtended()) retractBack();
        else extendBack();

        state = State.MANUAL;
    }

    /**
     * Retracts the cargo side solenoid
     */
    public void retractBack() {
        backSolenoid.set(Value.kForward);
    }

    /**
     * Extends the cargo side solenoid
     */
    public void extendBack() {
        backSolenoid.set(Value.kReverse);
    }

    /**
     * Freezes the cargo side solenoid
     */
    public void turnOffBack() {
        backSolenoid.set(Value.kOff);
    }

    /** 
     * Go to the next state of the Level 3 climb
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
            break;
            case SECONDARY_FREEZE:
            break;
            case MANUAL:
            break;
        }
    }

    /** 
     * Go to the previous state of the Level 3 climb
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
            break;
            case SECONDARY_FREEZE:
            break;
            case MANUAL:
            break;
        }
    }

    /** 
     * Advance our Level 2 climb
     */
    public void advanceSecondaryClimb() {
        switch(state) {
            case GROUND:
                state = State.SECONDARY_RAISE;
            break;
            case EXTENDED:
            break;
            case HALF:
            break;
            case SECONDARY_RAISE:
                state = State.SECONDARY_FREEZE;
            break;
            case SECONDARY_FREEZE:
                state = State.GROUND;
            break;
            case MANUAL:
            break;
        }
    }

    /**
     * Set a speed to the climb sub-drivetrain
     * 
     * @param speed speed for the climb sub-drivetrain
     */
    public void climbDrive(double speed) {
        double adjustedSpeed = speed * RobotMap.CLIMB_DRIVE_MAX_SPEED;
        frontSpeed = adjustedSpeed;
        backSpeed = adjustedSpeed;
    }

    /**
     * @return whether or not the front (hatch side) is currently extended
     */
    public boolean isFrontExtended() {
        return frontSolenoid.get() == DoubleSolenoid.Value.kReverse;
    }

    
    /**
     * @return whether or not the back (cargo side) is currently extended
     */
    public boolean isBackExtended() {
        return backSolenoid.get() == DoubleSolenoid.Value.kReverse;
    }

    /**
     * Set up SmartDashboard/Shuffleboard for constant tuning
     */
    public void setConstantTuning() {
        
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

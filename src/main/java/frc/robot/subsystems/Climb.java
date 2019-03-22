package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.util.Gyro;

public class Climb extends Subsystem {

    private static Climb instance = null;

    private DoubleSolenoid frontSolenoid;
    private DoubleSolenoid backSolenoid;

    private WPI_VictorSPX frontMotor;
    private WPI_VictorSPX backMotor;

    private double frontSpeed;
    private double backSpeed;

    public static enum State {
        GROUND, EXTENDED, HALF, MANUAL
    }
    private State state = State.GROUND;

    private Climb() {
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

        Gyro.getInstance().zeroClimbTiltAngle();
        state = State.GROUND;
    }

    public void update() {
        switch(state) {
            case GROUND:
                retractFront();
                retractFront();
            break;
            case EXTENDED: // Climb Stabilizer
                double angle = Gyro.getInstance().getClimbTiltAngle();
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
            case MANUAL:
                // Once this state is reached, the climb will only be actuated through manual procedures until reset
                break;
        }

        climbDrive(OI.getInstance().getClimbDriveSpeed());

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

    public void toggleFront() {
        if(isFrontExtended()) retractFront();
        else extendFront();

        state = State.MANUAL;
    }

    public void retractFront() {
        frontSolenoid.set(Value.kForward);
    }

    public void extendFront() {
        frontSolenoid.set(Value.kReverse); 
    }

    public void turnOffFront() {
        frontSolenoid.set(Value.kOff);
    }

    public void toggleBack() {
        if(isBackExtended()) retractBack();
        else extendBack();

        state = State.MANUAL;
    }

    public void retractBack() {
        backSolenoid.set(Value.kForward);
    }

    public void extendBack() {
        backSolenoid.set(Value.kReverse);
    }

    public void turnOffBack() {
        backSolenoid.set(Value.kOff);
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
            case MANUAL:
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
            case MANUAL:
            break;
        }
    }

    public void climbDrive(double speed) {
        double adjustedSpeed = speed * RobotMap.CLIMB_DRIVE_MAX_SPEED;
        frontSpeed = adjustedSpeed;
        backSpeed = adjustedSpeed;
    }

    // Whether or not the front is currently extended
    public boolean isFrontExtended() {
        return frontSolenoid.get() == DoubleSolenoid.Value.kReverse;
    }

    // Whether or not the back is currently extended
    public boolean isBackExtended() {
        return backSolenoid.get() == DoubleSolenoid.Value.kReverse;
    }

    public void setConstantTuning() {
        
    }

    public void getConstantTuning() {
        
    }

    public State getState() {
        return state;
    }

    public static Climb getInstance() {
        if (instance == null) {
            instance = new Climb();
        }
        return instance;
    }
}

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.RobotMap;
import frc.robot.commands.DriveCommand;
import frc.robot.util.Gyro;
import frc.robot.util.SynchronousPIDF;

/**
 * Subsystem to handle the drivetrain
 * 
 * - Utilizes 4 NEO motors, 2 for each side
 * 
 * - Uses a gyro to perform precise turns using PIDF control
 * 
 * - Implements the ability to reverse the direction of the drive
 */

public class Drivetrain extends SubsystemBase {

    // Constants
    public static final double DRIVE_FORWARD_MAX_SPEED = 1.0; // percentage
    public static final double DRIVE_TURN_MAX_SPEED = 0.8; // percentage

    public static double[] DRIVE_TURN_PIDF = {0.01, 0.0, 0.0, 0.0};
    public static double DRIVE_TURN_PID_TOLERANCE = 3.0; // degrees
    public static double DRIVE_TURN_PID_WAIT = 2.0; // seconds
    public static double DRIVE_TURN_PID_MAX_OUTPUT = 0.8; // percentage
    public static double DRIVE_TURN_PID_MIN_OUTPUT = -0.8; // percentage

    private CANSparkMax flDrive;
    private CANSparkMax frDrive;
    private CANSparkMax blDrive;
    private CANSparkMax brDrive;

    private DifferentialDrive drivetrain;
    private SynchronousPIDF pidController;

    private double driveSpeed;
    private double turnSpeed;
    private boolean reversed;

    /**
     * DRIVE - normal driver control
     * 
     * PID_TURN - turning to a specific angle
     */
    public enum State {
        DRIVER, PID_TURN
    }

    private State state = State.DRIVER;

    public Drivetrain() {
        flDrive = new CANSparkMax(RobotMap.DRIVE_FRONT_LEFT_ID, MotorType.kBrushless);
        frDrive = new CANSparkMax(RobotMap.DRIVE_FRONT_RIGHT_ID, MotorType.kBrushless);
        blDrive = new CANSparkMax(RobotMap.DRIVE_BACK_LEFT_ID, MotorType.kBrushless);
        brDrive = new CANSparkMax(RobotMap.DRIVE_BACK_RIGHT_ID, MotorType.kBrushless);

        flDrive.restoreFactoryDefaults();
        frDrive.restoreFactoryDefaults();
        blDrive.restoreFactoryDefaults();
        brDrive.restoreFactoryDefaults();

        flDrive.setIdleMode(IdleMode.kBrake);
        frDrive.setIdleMode(IdleMode.kBrake);
        blDrive.setIdleMode(IdleMode.kCoast);
        brDrive.setIdleMode(IdleMode.kCoast);

        flDrive.setSmartCurrentLimit(RobotMap.NEO_CURRENT_LIMIT);
        frDrive.setSmartCurrentLimit(RobotMap.NEO_CURRENT_LIMIT);
        blDrive.setSmartCurrentLimit(RobotMap.NEO_CURRENT_LIMIT);
        brDrive.setSmartCurrentLimit(RobotMap.NEO_CURRENT_LIMIT);

        blDrive.follow(flDrive);
        brDrive.follow(frDrive);

        drivetrain = new DifferentialDrive(flDrive, frDrive);
        pidController = new SynchronousPIDF(DRIVE_TURN_PIDF[0], DRIVE_TURN_PIDF[1],
                DRIVE_TURN_PIDF[2], DRIVE_TURN_PIDF[3]);
        pidController.setOutputRange(DRIVE_TURN_PID_MIN_OUTPUT, DRIVE_TURN_PID_MAX_OUTPUT);

        driveSpeed = 0;
        turnSpeed = 0;
        reversed = false;

        setConstantTuning();
        reset();

        setDefaultCommand(new DriveCommand());
    }

    private void reset() {
        flDrive.set(0);
        frDrive.set(0);
        blDrive.set(0);
        brDrive.set(0);

        reversed = false;
        state = State.DRIVER;
    }

    /**
     * Update motor outputs according to the current state
     */
    public void update(double deltaT) {
        switch (state) {
            case DRIVER:
                drivetrain.arcadeDrive(driveSpeed, turnSpeed);
                break;
            case PID_TURN:
                drivetrain.arcadeDrive(0,
                        pidController.calculate(Gyro.getInstance().getRobotAngle(), deltaT));
                break;
        }

        driveSpeed = 0;
        turnSpeed = 0;
    }

    public void outputValues() {
        SmartDashboard.putBoolean("Drive Reversed", reversed);

        SmartDashboard.putNumber("Drive FL Current", flDrive.getOutputCurrent());
        SmartDashboard.putNumber("Drive FR Current", frDrive.getOutputCurrent());
        SmartDashboard.putNumber("Drive BL Current", blDrive.getOutputCurrent());
        SmartDashboard.putNumber("Drive BR Current", brDrive.getOutputCurrent());
    }

    /**
     * Arcade drive with a forward speed and turn speed Applies drive reverse
     *
     * @param x forward speed
     * @param z turn speed
     */
    public void drive(double x, double z) {
        if (reversed) {
            driveSpeed = -x * DRIVE_FORWARD_MAX_SPEED;
            turnSpeed = z * DRIVE_TURN_MAX_SPEED;
        } else {
            driveSpeed = x * DRIVE_FORWARD_MAX_SPEED;
            turnSpeed = z * DRIVE_TURN_MAX_SPEED;
        }
        if (driveSpeed != 0.0 || turnSpeed != 0.0) {
            state = State.DRIVER;
        }
    }

    /**
     * Turn 90 deg counterclockwise
     */
    public void turnLeft() {
        Gyro.getInstance().zeroRobotAngle();
        pidController.reset();
        pidController.setSetpoint(-90);
        state = State.PID_TURN;
    }

    /**
     * Turn 90 deg clockwise
     */
    public void turnRight() {
        Gyro.getInstance().zeroRobotAngle();
        pidController.reset();
        pidController.setSetpoint(90);
        state = State.PID_TURN;
    }

    /**
     * Toggle the reverse drive feature
     */
    public void toggleReverse() {
        reversed = !reversed;
    }

    /**
     * Set up SmartDashboard/Shuffleboard for constant tuning
     */
    private void setConstantTuning() {
        SmartDashboard.putNumber("Drive Turn P", DRIVE_TURN_PIDF[0]);
        SmartDashboard.putNumber("Drive Turn I", DRIVE_TURN_PIDF[1]);
        SmartDashboard.putNumber("Drive Turn D", DRIVE_TURN_PIDF[2]);
        SmartDashboard.putNumber("Drive Turn F", DRIVE_TURN_PIDF[3]);
    }

    /**
     * Retrieves constant tuning from SmartDashboard/Shuffleboard
     */
    public void getConstantTuning() {
        DRIVE_TURN_PIDF[0] = SmartDashboard.getNumber("Drive Turn P", DRIVE_TURN_PIDF[0]);
        DRIVE_TURN_PIDF[1] = SmartDashboard.getNumber("Drive Turn I", DRIVE_TURN_PIDF[1]);
        DRIVE_TURN_PIDF[2] = SmartDashboard.getNumber("Drive Turn D", DRIVE_TURN_PIDF[2]);
        DRIVE_TURN_PIDF[3] = SmartDashboard.getNumber("Drive Turn F", DRIVE_TURN_PIDF[3]);

        pidController.setPID(DRIVE_TURN_PIDF[0], DRIVE_TURN_PIDF[1], DRIVE_TURN_PIDF[2],
                DRIVE_TURN_PIDF[3]);
    }

    public State getState() {
        return state;
    }
}

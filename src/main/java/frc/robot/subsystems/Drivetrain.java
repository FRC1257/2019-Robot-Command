package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.drivetrain.*;
import frc.robot.util.Gyro;
import frc.robot.util.SynchronousPIDF;

/**
 * Subsystem to handle our 4 NEO Tank Drivetrain
 *  - Utilizes PID for 90 deg turns
 *  - Can toggle reverse driving
 */

public class Drivetrain extends Subsystem {

    private CANSparkMax flDrive;
    private CANSparkMax frDrive;
    private CANSparkMax blDrive;
    private CANSparkMax brDrive;

    private DifferentialDrive drivetrain;
    private Gyro gyro;
    private SynchronousPIDF pidController;

    private double driveSpeed;
    private double turnSpeed;
    private boolean reversed;

    /**
     * DRIVER - Manual driver control
     * PID_TURN - Using PID to execute a 90 deg turn
     */
    public static enum State {
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
        gyro = Robot.gyro;
        pidController = new SynchronousPIDF(RobotMap.DRIVE_TURN_PIDF[0], RobotMap.DRIVE_TURN_PIDF[1], 
            RobotMap.DRIVE_TURN_PIDF[2], RobotMap.DRIVE_TURN_PIDF[3]);

        driveSpeed = 0;
        turnSpeed = 0;
        reversed = false;

        setConstantTuning();
        reset();
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new DriveCommand());
    }

    public void reset() {
        flDrive.set(0);
        frDrive.set(0);
        blDrive.set(0);
        brDrive.set(0);

        reversed = false;
        state = State.DRIVER;
    }

    /**
     * Update motor outputs according to the current state
     * Use dt for PID calculations
     * Output debugging values
     */
    public void update(double deltaT) {
        switch(state) {
            case DRIVER:
                drivetrain.arcadeDrive(driveSpeed, turnSpeed);
            break;
            case PID_TURN:
                drivetrain.arcadeDrive(0, pidController.calculate(gyro.getRobotAngle(), deltaT));

                double error = Math.abs(gyro.getRobotAngle() - pidController.getSetpoint());
                if(error < RobotMap.DRIVE_TURN_PID_TOLERANCE) {
                    state = State.DRIVER;
                }
            break;
        }

        driveSpeed = 0;
        turnSpeed = 0;

        SmartDashboard.putString("Drive State", state.name());
        SmartDashboard.putBoolean("Drive Reversed", reversed);

        SmartDashboard.putNumber("Drive FL Current", flDrive.getOutputCurrent());
        SmartDashboard.putNumber("Drive FR Current", frDrive.getOutputCurrent());
        SmartDashboard.putNumber("Drive BL Current", blDrive.getOutputCurrent());
        SmartDashboard.putNumber("Drive BR Current", brDrive.getOutputCurrent());

        SmartDashboard.putNumber("Drive FL Temperature (C)", flDrive.getMotorTemperature());
        SmartDashboard.putNumber("Drive FR Temperature (C)", frDrive.getMotorTemperature());
        SmartDashboard.putNumber("Drive BL Temperature (C)", blDrive.getMotorTemperature());
        SmartDashboard.putNumber("Drive BR Temperature (C)", brDrive.getMotorTemperature());
    }

    /**
     * Drive at a specific forward speed and turn speed.
     * Will end PID if called
     * 
     * @param x forward speed
     * @param z turn speed
     */
    public void drive(double x, double z) {
        if(reversed) {
            driveSpeed = -x * RobotMap.DRIVE_FORWARD_MAX_SPEED;
            turnSpeed = z * RobotMap.DRIVE_TURN_MAX_SPEED;
        }
        else {
            driveSpeed = x * RobotMap.DRIVE_FORWARD_MAX_SPEED;
            turnSpeed = z * RobotMap.DRIVE_TURN_MAX_SPEED;
        }
        if(driveSpeed != 0.0 || turnSpeed != 0.0) {
            state = State.DRIVER;
        }
    }

    /**
     * Begin a PID turn for 90 deg counterclockwise
     */
    public void turnLeft() {
        gyro.zeroRobotAngle();
        pidController.reset();
        pidController.setSetpoint(-90);
        state = State.PID_TURN;
    }

    /**
     * Begin a PID turn for 90 deg clockwise
     */
    public void turnRight() {
        gyro.zeroRobotAngle();
        pidController.reset();
        pidController.setSetpoint(90);
        state = State.PID_TURN;
    }

    /**
     * Toggle the reverse drive
     */
    public void toggleReverse() {
        reversed = !reversed;
    }

    /**
     * Set up SmartDashboard/Shuffleboard for constant tuning
     */
    public void setConstantTuning() {
        SmartDashboard.putNumber("Drive Turn P", RobotMap.DRIVE_TURN_PIDF[0]);
        SmartDashboard.putNumber("Drive Turn I", RobotMap.DRIVE_TURN_PIDF[1]);
        SmartDashboard.putNumber("Drive Turn D", RobotMap.DRIVE_TURN_PIDF[2]);
        SmartDashboard.putNumber("Drive Turn F", RobotMap.DRIVE_TURN_PIDF[3]);
    }

    /**
     * Retrieves constant tuning from SmartDashboard/Shuffleboard
     */
    public void getConstantTuning() {
        RobotMap.DRIVE_TURN_PIDF[0] = SmartDashboard.getNumber("Drive Turn P", RobotMap.DRIVE_TURN_PIDF[0]);
        RobotMap.DRIVE_TURN_PIDF[1] = SmartDashboard.getNumber("Drive Turn I", RobotMap.DRIVE_TURN_PIDF[1]);
        RobotMap.DRIVE_TURN_PIDF[2] = SmartDashboard.getNumber("Drive Turn D", RobotMap.DRIVE_TURN_PIDF[2]);
        RobotMap.DRIVE_TURN_PIDF[3] = SmartDashboard.getNumber("Drive Turn F", RobotMap.DRIVE_TURN_PIDF[3]);

        pidController.setPID(RobotMap.DRIVE_TURN_PIDF[0], RobotMap.DRIVE_TURN_PIDF[1], 
            RobotMap.DRIVE_TURN_PIDF[2], RobotMap.DRIVE_TURN_PIDF[3]);
    }

    public State getState() {
        return state;
    }
}

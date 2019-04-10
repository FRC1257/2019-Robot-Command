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
        gyro = Robot.gyro;
        pidController = new SynchronousPIDF(RobotMap.DRIVE_TURN_PIDF[0], RobotMap.DRIVE_TURN_PIDF[1], 
            RobotMap.DRIVE_TURN_PIDF[2], RobotMap.DRIVE_TURN_PIDF[3]);
        pidController.setOutputRange(RobotMap.DRIVE_TURN_PID_MIN_OUTPUT, RobotMap.DRIVE_TURN_PID_MAX_OUTPUT);

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

    private void reset() {
        flDrive.set(0);
        frDrive.set(0);
        blDrive.set(0);
        brDrive.set(0);

        reversed = false;
        state = State.DRIVER;
    }

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
    }

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

    public void turnLeft() {
        gyro.zeroRobotAngle();
        pidController.reset();
        pidController.setSetpoint(-90);
        state = State.PID_TURN;
    }

    public void turnRight() {
        gyro.zeroRobotAngle();
        pidController.reset();
        pidController.setSetpoint(90);
        state = State.PID_TURN;
    }

    public void endTurn() {
        state = State.DRIVER;
    }

    public void toggleReverse() {
        reversed = !reversed;
    }

    private void setConstantTuning() {
        SmartDashboard.putNumber("Drive Turn P", RobotMap.DRIVE_TURN_PIDF[0]);
        SmartDashboard.putNumber("Drive Turn I", RobotMap.DRIVE_TURN_PIDF[1]);
        SmartDashboard.putNumber("Drive Turn D", RobotMap.DRIVE_TURN_PIDF[2]);
        SmartDashboard.putNumber("Drive Turn F", RobotMap.DRIVE_TURN_PIDF[3]);
    }

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

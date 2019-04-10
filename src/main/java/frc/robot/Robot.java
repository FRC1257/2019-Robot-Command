package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.cameraserver.CameraServer;

import frc.robot.subsystems.*;
import frc.robot.subsystems.cargointake.*;
import frc.robot.util.*;

public class Robot extends TimedRobot {

    // Subsystems
    public static Drivetrain drivetrain;
    public static Climb climb;
    public static CargoArm cargoArm;
    public static CargoRoller cargoRoller;
    public static HatchIntake hatchIntake;

    // Extra Systems
    public static Vision vision;
    public static Gyro gyro;
    public static OI oi;

    private PowerDistributionPanel pdp;
    
    // Timestamp of last iteration
    private double lastTimeStamp;

    @Override
    public void robotInit() {
        drivetrain = new Drivetrain();
        climb = new Climb();
        cargoArm = new CargoArm();
        cargoRoller = new CargoRoller();
        hatchIntake = new HatchIntake();

        vision = Vision.getInstance();
        gyro = Gyro.getInstance();
        oi = OI.getInstance();

        pdp = new PowerDistributionPanel();

        CameraServer.getInstance().startAutomaticCapture(0);

        lastTimeStamp = Timer.getFPGATimestamp();
    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        updateSubsystems();
    }

    @Override
    public void teleopInit() {

    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        updateSubsystems();
    }

    @Override
    public void testPeriodic() {
        Scheduler.getInstance().run();
        updateSubsystems();
        getSubsystemConstants();
    }

    /** 
     * Runs the update loop of every subsystem
     *  - Sends values to motor controllers
     *  - Updates internal states of subsystems
     *  - Outputs to SmartDashboard/Shuffleboard
     */

    private void updateSubsystems() {
        vision.update();

        double deltaT = Timer.getFPGATimestamp() - lastTimeStamp;
        drivetrain.update(deltaT);
        climb.update();
        cargoArm.update();
        cargoRoller.update();
        hatchIntake.update();
        gyro.outputValues();

        SmartDashboard.putNumber("PDP Temperature (C)", pdp.getTemperature());
        SmartDashboard.putData(pdp);
        SmartDashboard.putNumber("dT", deltaT);

        lastTimeStamp = Timer.getFPGATimestamp();
    }

    /**
     * Updates subsystem constants from SmartDashboard/Shuffleboard
     */
    private void getSubsystemConstants() {
        drivetrain.getConstantTuning();
        climb.getConstantTuning();
        cargoArm.getConstantTuning();
        cargoRoller.getConstantTuning();
        hatchIntake.getConstantTuning();

        vision.getConstantTuning();
    }
}

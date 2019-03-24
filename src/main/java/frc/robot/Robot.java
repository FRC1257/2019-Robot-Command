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

    public static Drivetrain drivetrain;
    public static Climb climb;
    public static CargoArm cargoArm;
    public static CargoRoller cargoRoller;
    public static HatchIntake hatchIntake;

    private Vision vision;
    private OI oi;
    private Gyro gyro;

    private PowerDistributionPanel pdp;
    
    private double lastTimeStamp;

    @Override
    public void robotInit() {
        drivetrain = new Drivetrain();
        climb = new Climb();
        cargoArm = new CargoArm();
        cargoRoller = new CargoRoller();
        hatchIntake = new HatchIntake();

        vision = Vision.getInstance();
        oi = OI.getInstance();
        gyro = Gyro.getInstance();

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

    public void updateSubsystems() {
        vision.update();
        
        drivetrain.update(Timer.getFPGATimestamp() - lastTimeStamp);
        climb.update();
        cargoArm.update();
        cargoRoller.update();
        hatchIntake.update();
        gyro.outputValues();

        SmartDashboard.putNumber("PDP Temperature (C)", pdp.getTemperature());
        SmartDashboard.putData(pdp);

        lastTimeStamp = Timer.getFPGATimestamp();
    }

    public void getSubsystemConstants() {
        drivetrain.getConstantTuning();
        climb.getConstantTuning();
        cargoArm.getConstantTuning();
        cargoRoller.getConstantTuning();
        hatchIntake.getConstantTuning();

        vision.getConstantTuning();
    }
}

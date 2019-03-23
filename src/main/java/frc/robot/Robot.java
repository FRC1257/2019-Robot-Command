package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;

import frc.robot.subsystems.*;
import frc.robot.subsystems.cargointake.*;
import frc.robot.util.*;

public class Robot extends TimedRobot {

    public static Drivetrain drivetrain;
    public static Climb climb;
    public static CargoArm cargoArm;
    public static CargoRoller cargoRoller;
    public static HatchIntake hatchIntake;

    private OI oi;
    private Gyro gyro;
    
    private double lastTimeStamp;

    @Override
    public void robotInit() {
        drivetrain = new Drivetrain();
        climb = new Climb();
        cargoArm = new CargoArm();
        cargoRoller = new CargoRoller();
        hatchIntake = new HatchIntake();

        oi = OI.getInstance();
        gyro = Gyro.getInstance();

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
        drivetrain.update(Timer.getFPGATimestamp() - lastTimeStamp);
        climb.update();
        cargoArm.update();
        cargoRoller.update();
        hatchIntake.update();

        gyro.outputValues();

        lastTimeStamp = Timer.getFPGATimestamp();
    }

    public void getSubsystemConstants() {
        drivetrain.getConstantTuning();
        climb.getConstantTuning();
        cargoArm.getConstantTuning();
        cargoRoller.getConstantTuning();
        hatchIntake.getConstantTuning();
    }
}

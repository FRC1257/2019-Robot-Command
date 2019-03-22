package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;

import frc.robot.subsystems.*;
import frc.robot.subsystems.hatchintake.*;
import frc.robot.subsystems.cargointake.*;
import frc.robot.util.*;

public class Robot extends TimedRobot {

    private Drivetrain drivetrain;
    private Climb climb;
    private CargoArm cargoArm;
    private CargoRoller cargoIntake;
    private HatchPivot hatchPivot;
    private HatchSolenoids hatchSolenoids;

    private OI oi;
    private Gyro gyro;
    
    private double lastTimeStamp;

    @Override
    public void robotInit() {
        drivetrain = Drivetrain.getInstance();
        climb = Climb.getInstance();
        cargoArm = CargoArm.getInstance();
        cargoIntake = CargoRoller.getInstance();
        hatchPivot = HatchPivot.getInstance();
        hatchSolenoids = HatchSolenoids.getInstance();

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
        cargoIntake.update();
        hatchPivot.update();
        hatchSolenoids.update();

        gyro.outputValues();

        lastTimeStamp = Timer.getFPGATimestamp();
    }

    public void getSubsystemConstants() {
        drivetrain.getConstantTuning();
        climb.getConstantTuning();
        cargoArm.getConstantTuning();
        cargoIntake.getConstantTuning();
        hatchPivot.getConstantTuning();
        hatchSolenoids.getConstantTuning();
    }
}

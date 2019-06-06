package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.cscore.UsbCamera;
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
    public static Gyro gyro;
    public static OI oi;

    private PowerDistributionPanel pdp;
    
    private double lastTimeStamp; // timestamp of last iteration
    private int outputCounter; // counter for staggering output calls
    private int tuningCounter; // counter for staggering tuning calls

    @Override
    public void robotInit() {
        drivetrain = new Drivetrain();
        climb = new Climb();
        cargoArm = new CargoArm();
        cargoRoller = new CargoRoller();
        hatchIntake = new HatchIntake();

        gyro = Gyro.getInstance();
        oi = OI.getInstance();

        pdp = new PowerDistributionPanel();

        UsbCamera camera0 = CameraServer.getInstance().startAutomaticCapture(0);
        UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(1);

        camera0.setFPS(20);
        camera1.setFPS(20);
        camera0.setResolution(300, 300);
        camera1.setResolution(300, 300);

        lastTimeStamp = Timer.getFPGATimestamp();
        outputCounter = 0;
        tuningCounter = 0;

        LiveWindow.disableAllTelemetry();
    }

    @Override
    public void robotPeriodic() {
        Scheduler.getInstance().run();
        updateSubsystems();
        outputValues();
    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void autonomousPeriodic() {
        
    }

    @Override
    public void teleopInit() {

    }

    @Override
    public void teleopPeriodic() {
        
    }

    @Override
    public void testPeriodic() {
        getSubsystemConstants();
    }

    /** 
     * Runs the update loop of every subsystem
     *  - Sends values to motor controllers
     *  - Updates internal states of subsystems
     *  - Outputs to SmartDashboard/Shuffleboard
     */
    private void updateSubsystems() {
        double deltaT = Timer.getFPGATimestamp() - lastTimeStamp;
        drivetrain.update(deltaT);
        climb.update();
        cargoArm.update();
        cargoRoller.update();
        hatchIntake.update();

        lastTimeStamp = Timer.getFPGATimestamp();
    }

    /**
     * Outputs SmartDashboard/Shuffleboard values for all subsystems in a staggered way to avoid lag/memory issues
     */
    private void outputValues() {
        switch(outputCounter) {
            case 0:
                drivetrain.outputValues();
            break;
            case 1:
                climb.outputValues();
            break;
            case 2:
                cargoArm.outputValues();
            break;
            case 3:
                cargoRoller.outputValues();
            break;
            case 4:
                hatchIntake.outputValues();
            break;
            case 5:
                gyro.outputValues();
            break;
            case 6:
                SmartDashboard.putData(pdp);
            break;
        }
        outputCounter = (outputCounter + 1) % 7;
    }

    /**
     * Updates subsystem constants from SmartDashboard/Shuffleboard in a staggered way to avoid lag/memory issues
     */
    private void getSubsystemConstants() {
        switch(tuningCounter) {
            case 0:
                drivetrain.getConstantTuning();
            break;
            case 1:
                climb.getConstantTuning();
            break;
            case 2:
                cargoArm.getConstantTuning();
            break;
            case 3:
                cargoRoller.getConstantTuning();
            break;
            case 4:
                hatchIntake.getConstantTuning();
            break;
        }

        tuningCounter = (tuningCounter + 1) % 5;
    }
}

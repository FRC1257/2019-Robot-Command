package frc.robot;

import frc.robot.commands.drivetrain.*;
import frc.robot.commands.cargointake.cargoarm.*;
import frc.robot.commands.cargointake.cargoroller.*;
import frc.robot.commands.climb.*;
import frc.robot.commands.hatchintake.*;
import frc.robot.util.SnailController;
import static frc.robot.util.SnailController.*;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */

public class OI {

    private static OI instance = null;

    private SnailController driveController;
    private SnailController operatorController;

    private OI() {
        driveController = new SnailController(RobotMap.CONTROLLER_DRIVE_PORT);
        operatorController = new SnailController(RobotMap.CONTROLLER_OPERATOR_PORT);

        // Drive
        driveController.yButton.whenPressed(new ReverseDriveCommand());
        driveController.xButton.whenPressed(new TurnLeftCommand());
        driveController.bButton.whenPressed(new TurnRightCommand());

        // Cargo Intake
        operatorController.aButton.whileHeld(new EjectCargoCommand());
        operatorController.bButton.whileHeld(new IntakeCargoCommand());

        // Climb
        operatorController.startButton.whenPressed(new AdvanceClimbCommand());
        operatorController.selectButton.whenPressed(new BackClimbCommand());
        driveController.startButton.whenPressed(new ResetClimbCommand());
        driveController.selectButton.whenPressed(new AdvanceSecondaryClimbCommand());

        // Intake Arm
        operatorController.leftBumper.whenPressed(new MoveCargoCommand());
        operatorController.rightBumper.whenPressed(new MoveCargoCommand());

        // Hatch Intake
        operatorController.xButton.whileHeld(new EjectHatchCommand());
        operatorController.yButton.whileHeld(new IntakeHatchCommand());
    }

    // Drive
    public double getDriveForwardSpeed() {
        return driveController.getForwardSpeed();
    }

    public double getDriveTurnSpeed() {
        return driveController.getTurnSpeed();
    }

    // Climb
    public double getClimbDriveSpeed() {
        return squareInput(driveController.getForwardSpeed());
    }

    // Cargo Arm
    public double getCargoArmSpeed() {
        return squareInput(operatorController.getRightStickY());
    }

    // Vision
    public double getTurnCorrect() {
        return driveController.getLeftTrigger();
    }

    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }
}

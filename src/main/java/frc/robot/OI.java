package frc.robot;

import frc.robot.commands.drivetrain.*;
import frc.robot.commands.cargointake.cargoarm.*;
import frc.robot.commands.cargointake.cargoroller.*;
import frc.robot.commands.climb.*;
import frc.robot.commands.hatchintake.*;
import frc.robot.util.SnailController;
import static frc.robot.util.SnailController.*;

/**
 * This class is the glue that binds the controls on the physical operator interface to the commands
 * and command groups that allow control of the robot.
 */

public class OI {
    
    private static OI instance = null;

    private SnailController driveController;
    private SnailController operatorController;

    public enum ControlScheme {
        ORIGINAL, MODIFIED
    }

    public ControlScheme controlScheme = ControlScheme.MODIFIED;

    private OI() {
        driveController = new SnailController(RobotMap.CONTROLLER_DRIVE_PORT);
        operatorController = new SnailController(RobotMap.CONTROLLER_OPERATOR_PORT);

        switch (controlScheme) {
            case ORIGINAL:
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

                // Cargo Arm
                operatorController.leftBumper.whenPressed(new MoveCargoCommand());
                operatorController.rightBumper.whenPressed(new MoveRocketCommand());
                operatorController.rightStickButton.whenPressed(new FreezeArmCommand());

                // Hatch Intake
                operatorController.xButton.whileHeld(new EjectHatchCommand());
                operatorController.yButton.whileHeld(new IntakeHatchCommand());
                break;
            case MODIFIED:
                // Drive
                driveController.yButton.whenPressed(new ReverseDriveCommand());
                driveController.xButton.whenPressed(new TurnLeftCommand());
                driveController.bButton.whenPressed(new TurnRightCommand());

                // Cargo Intake
                operatorController.rightTrigger.whileActive(new EjectCargoCommand());
                operatorController.leftTrigger.whileActive(new IntakeCargoCommand());

                // Climb
                operatorController.startButton.whenPressed(new AdvanceClimbCommand());
                operatorController.selectButton.whenPressed(new BackClimbCommand());
                driveController.startButton.whenPressed(new ResetClimbCommand());
                driveController.selectButton.whenPressed(new AdvanceSecondaryClimbCommand());

                // Cargo Arm
                operatorController.aButton.whenPressed(new MoveCargoCommand());
                operatorController.bButton.whenPressed(new MoveRocketCommand());
                operatorController.leftStickButton.whenPressed(new FreezeArmCommand());
                break;
        }
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
        switch (controlScheme) {
            case ORIGINAL:
                return squareInput(-operatorController.getRightStickY());
            case MODIFIED:
                return squareInput(operatorController.getLeftStickY());
            default:
                return 0;
        }
    }

    public double getHatchIntakeSpeed() {
        switch (controlScheme) {
            case ORIGINAL:
                return 0;
            case MODIFIED:
                return squareInput(operatorController.getRightStickY());
            default:
                return 0;
        }
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

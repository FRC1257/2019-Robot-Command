package frc.robot;

import frc.robot.util.SnailController;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;

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
                driveController.yButton.whenPressed(new InstantCommand(() -> Robot.drivetrain.toggleReverse(), Robot.drivetrain));
                driveController.xButton.whenPressed(new InstantCommand(() -> Robot.drivetrain.turnLeft(), Robot.drivetrain));
                driveController.bButton.whenPressed(new InstantCommand(() -> Robot.drivetrain.turnRight(), Robot.drivetrain));

                // Cargo Roller
                operatorController.aButton.whileHeld(new RunCommand(() -> Robot.cargoRoller.eject(), Robot.cargoRoller));
                operatorController.bButton.whileHeld(new RunCommand(() -> Robot.cargoRoller.intake(), Robot.cargoRoller));

                // Climb
                operatorController.startButton.whenPressed(new InstantCommand(() -> Robot.climb.advanceClimb(), Robot.climb));
                operatorController.selectButton.whenPressed(new InstantCommand(() -> Robot.climb.backClimb(), Robot.climb));
                driveController.startButton.whenPressed(new InstantCommand(() -> Robot.climb.reset(), Robot.climb));
                driveController.selectButton.whenPressed(new InstantCommand(() -> Robot.climb.advanceSecondaryClimb(), Robot.climb));

                // Cargo Arm
                operatorController.leftBumper.whenPressed(new InstantCommand(() -> Robot.cargoArm.moveCargo(), Robot.cargoArm));
                operatorController.rightBumper.whenPressed(new InstantCommand(() -> Robot.cargoArm.moveRocket(), Robot.cargoArm));
                operatorController.rightStickButton.whenPressed(new InstantCommand(() -> Robot.cargoArm.freeze(), Robot.cargoArm));

                // Hatch Intake
                operatorController.xButton.whileHeld(new RunCommand(() -> Robot.hatchIntake.intake(), Robot.hatchIntake));
                operatorController.yButton.whileHeld(new RunCommand(() -> Robot.hatchIntake.eject(), Robot.hatchIntake));
                break;

            case MODIFIED:
                // Drive
                driveController.yButton.whenPressed(new InstantCommand(() -> Robot.drivetrain.toggleReverse(), Robot.drivetrain));
                driveController.xButton.whenPressed(new InstantCommand(() -> Robot.drivetrain.turnLeft(), Robot.drivetrain));
                driveController.bButton.whenPressed(new InstantCommand(() -> Robot.drivetrain.turnRight(), Robot.drivetrain));

                // Cargo Roller
                operatorController.rightTrigger.whileActiveContinuous(new RunCommand(() -> Robot.cargoRoller.eject(), Robot.cargoRoller));
                operatorController.leftTrigger.whileActiveContinuous(new RunCommand(() -> Robot.cargoRoller.intake(), Robot.cargoRoller));

                // Climb
                operatorController.startButton.whenPressed(new InstantCommand(() -> Robot.climb.advanceClimb(), Robot.climb));
                operatorController.selectButton.whenPressed(new InstantCommand(() -> Robot.climb.backClimb(), Robot.climb));
                driveController.startButton.whenPressed(new InstantCommand(() -> Robot.climb.reset(), Robot.climb));
                driveController.selectButton.whenPressed(new InstantCommand(() -> Robot.climb.advanceSecondaryClimb(), Robot.climb));

                // Cargo Arm
                operatorController.aButton.whenPressed(new InstantCommand(() -> Robot.cargoArm.moveCargo(), Robot.cargoArm));
                operatorController.bButton.whenPressed(new InstantCommand(() -> Robot.cargoArm.moveRocket(), Robot.cargoArm));
                operatorController.leftStickButton.whenPressed(new InstantCommand(() -> Robot.cargoArm.freeze(), Robot.cargoArm));
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

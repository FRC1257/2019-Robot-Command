package frc.robot;

import frc.robot.commands.drivetrain.*;
import frc.robot.commands.cargointake.cargoarm.*;
import frc.robot.commands.cargointake.cargoroller.*;
import frc.robot.commands.climb.*;
import frc.robot.commands.hatchintake.hatchpivot.*;
import frc.robot.commands.hatchintake.hatchsolenoids.*;
import frc.robot.util.SnailController;
import static frc.robot.util.SnailController.*;

public class OI {

    /**
     * This class is the glue that binds the controls on the physical operator
     * interface to the commands and command groups that allow control of the robot.
     */

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

        // Intake Arm
        operatorController.leftBumper.whenPressed(new MoveCargoCommand());
        operatorController.rightBumper.whenPressed(new MoveCargoCommand());

        // Hatch Pivot
        operatorController.leftTrigger.whenActive(new RaisePivotCommand());
        operatorController.rightTrigger.whenActive(new LowerPivotCommand());

        // Hatch Solenoids
        operatorController.xButton.whileHeld(new EjectHatchCommand());
        operatorController.yButton.whileHeld(new PickupHatchCommand());
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
        return squareInput(operatorController.getY(Hand.kRight));
    }

    // Hatch Pivot
    public double getHatchPivotSpeed() {
        return squareInput(operatorController.getY(Hand.kLeft));
    }

    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }
}

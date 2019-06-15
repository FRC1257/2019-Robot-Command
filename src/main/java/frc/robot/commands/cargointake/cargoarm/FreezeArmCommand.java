package frc.robot.commands.cargointake.cargoarm;

import edu.wpi.first.wpilibj.command.InstantCommand;

import frc.robot.Robot;
import frc.robot.subsystems.cargointake.CargoArm;

/**
 * Freeze the cargo arm at the currently held position using PID control
 * 
 * Once this begins, the driver WILL be able to cancel it through controller
 * input
 */

public class FreezeArmCommand extends InstantCommand {

    private CargoArm cargoArm;

    public FreezeArmCommand() {
        super();

        cargoArm = Robot.cargoArm;
        requires(cargoArm);
    }

    @Override
    public void initialize() {
        cargoArm.freeze();
    }

    @Override
    public void end() {

    }

    @Override
    public void interrupted() {
        end();
    }
}

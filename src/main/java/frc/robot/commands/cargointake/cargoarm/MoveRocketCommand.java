package frc.robot.commands.cargointake.cargoarm;

import edu.wpi.first.wpilibj.command.InstantCommand;

import frc.robot.Robot;
import frc.robot.subsystems.cargointake.CargoArm;

/**
 * Begin moving the cargo arm to the position to score in the level 1 rocket
 * 
 * Once this begins, the driver WILL be able to cancel it through controller
 * input
 */

public class MoveRocketCommand extends InstantCommand {

    private CargoArm cargoArm;

    public MoveRocketCommand() {
        super();

        cargoArm = Robot.cargoArm;
        requires(cargoArm);
    }

    @Override
    public void initialize() {
        cargoArm.moveRocket();
    }

    @Override
    public void end() {

    }

    @Override
    public void interrupted() {
        end();
    }
}

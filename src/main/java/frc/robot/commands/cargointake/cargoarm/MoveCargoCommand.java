package frc.robot.commands.cargointake.cargoarm;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystems.cargointake.CargoArm;

/**
 * Begin moving the cargo arm to the position to score in the cargo bay
 * 
 * Once this begins, the driver WILL be able to cancel it through controller input
 */

public class MoveCargoCommand extends InstantCommand {

    private CargoArm cargoArm;

    public MoveCargoCommand() {
        super();

        cargoArm = Robot.cargoArm;
        requires(cargoArm);
    }

    @Override
    public void initialize() {
        cargoArm.moveCargo();
    }

    @Override
    public void end() {

    }

    @Override
    public void interrupted() {
        end();
    }
}

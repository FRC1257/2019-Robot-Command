package frc.robot.commands.cargointake.cargoroller;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.cargointake.CargoRoller;

/**
 * Eject a cargo ball
 */

public class EjectCargoCommand extends Command {

    private CargoRoller cargoIntake;

    public EjectCargoCommand() {
        cargoIntake = Robot.cargoRoller;

        requires(cargoIntake);
    }

    @Override
    public void execute() {
        cargoIntake.eject();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end() {
        cargoIntake.neutral();
    }

    @Override
    public void interrupted() {
        end();
    }
}

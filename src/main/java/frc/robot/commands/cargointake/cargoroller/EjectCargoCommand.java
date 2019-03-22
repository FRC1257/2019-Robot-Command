package frc.robot.commands.cargointake.cargoroller;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.subsystems.cargointake.CargoRoller;;

public class EjectCargoCommand extends Command {

    private CargoRoller cargoIntake;

    public EjectCargoCommand() {
        cargoIntake = CargoRoller.getInstance();

        requires(cargoIntake);
    }

    @Override
    protected void execute() {
        cargoIntake.eject();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
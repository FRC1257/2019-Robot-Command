package frc.robot.commands.cargointake.cargoroller;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.subsystems.cargointake.CargoRoller;;

public class NeutralCargoCommand extends Command {

    private CargoRoller cargoIntake;

    public NeutralCargoCommand() {
        cargoIntake = CargoRoller.getInstance();

        requires(cargoIntake);
    }

    @Override
    protected void execute() {
        cargoIntake.neutral();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
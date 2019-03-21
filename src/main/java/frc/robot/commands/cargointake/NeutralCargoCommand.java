package frc.robot.commands.cargointake;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.subsystems.CargoIntake;;

public class NeutralCargoCommand extends Command {

    private CargoIntake cargoIntake;

    public NeutralCargoCommand() {
        cargoIntake = CargoIntake.getInstance();

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
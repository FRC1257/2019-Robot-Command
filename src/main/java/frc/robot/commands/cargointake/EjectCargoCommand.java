package frc.robot.commands.cargointake;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.subsystems.CargoIntake;;

public class EjectCargoCommand extends Command {

    private CargoIntake cargoIntake;

    public EjectCargoCommand() {
        cargoIntake = CargoIntake.getInstance();

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
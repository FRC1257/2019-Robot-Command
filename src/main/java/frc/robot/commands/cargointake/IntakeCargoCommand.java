package frc.robot.commands.cargointake;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.subsystems.CargoIntake;;

public class IntakeCargoCommand extends Command {

    private CargoIntake cargoIntake;

    public IntakeCargoCommand() {
        cargoIntake = CargoIntake.getInstance();

        requires(cargoIntake);
    }

    @Override
    protected void execute() {
        cargoIntake.intake();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
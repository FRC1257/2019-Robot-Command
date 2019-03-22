package frc.robot.commands.cargointake.cargoroller;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.subsystems.cargointake.CargoRoller;

public class IntakeCargoCommand extends Command {

    private CargoRoller cargoIntake;

    public IntakeCargoCommand() {
        cargoIntake = CargoRoller.getInstance();

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
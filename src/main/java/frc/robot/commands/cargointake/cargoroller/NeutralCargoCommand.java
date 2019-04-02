package frc.robot.commands.cargointake.cargoroller;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.Robot;
import frc.robot.subsystems.cargointake.CargoRoller;;

public class NeutralCargoCommand extends Command {

    private CargoRoller cargoIntake;

    public NeutralCargoCommand() {
        cargoIntake = Robot.cargoRoller;

        requires(cargoIntake);
    }

    @Override
    public void execute() {
        cargoIntake.neutral();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end() {
        
    }

    @Override
    public void interrupted() {
        end();
    }
}
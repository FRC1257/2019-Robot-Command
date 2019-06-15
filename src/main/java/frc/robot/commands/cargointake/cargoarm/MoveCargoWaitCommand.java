package frc.robot.commands.cargointake.cargoarm;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

import frc.robot.Robot;
import frc.robot.subsystems.cargointake.CargoArm;

/**
 * Begin moving the cargo arm to the position to score in the cargo bay
 * 
 * Once this begins, the driver WILL NOT be able to cancel it through controller
 * input
 *
 * For use in autonomous routines
 */

public class MoveCargoWaitCommand extends Command {

    private CargoArm cargoArm;

    private double start;

    public MoveCargoWaitCommand() {
        super();

        cargoArm = Robot.cargoArm;
        requires(cargoArm);
    }

    @Override
    public void initialize() {
        cargoArm.moveCargo();
        start = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        return cargoArm.getState() == CargoArm.State.MANUAL
                || Timer.getFPGATimestamp() - start > CargoArm.CARGO_ARM_PID_WAIT;
    }

    @Override
    public void end() {

    }

    @Override
    public void interrupted() {
        end();
    }
}

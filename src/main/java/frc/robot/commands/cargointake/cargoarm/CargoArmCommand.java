package frc.robot.commands.cargointake.cargoarm;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.OI;
import frc.robot.subsystems.cargointake.CargoArm;

/**
 * Set the speed of the cargo arm with the value from the controller
 *
 * Default command of the CargoArm subsystem
 */

public class CargoArmCommand extends Command {

    private CargoArm cargoArm;
    private OI oi;

    public CargoArmCommand() {
        cargoArm = Robot.cargoArm;
        oi = OI.getInstance();

        requires(cargoArm);
    }

    @Override
    public void execute() {
        cargoArm.setSpeed(oi.getCargoArmSpeed());
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

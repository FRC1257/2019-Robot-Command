package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.OI;
import frc.robot.subsystems.cargointake.CargoArm;

/**
 * Set the speed of the cargo arm with the value from the controller
 *
 * Default command of the CargoArm subsystem
 */

public class CargoArmCommand extends CommandBase {

    private CargoArm cargoArm;
    private OI oi;

    public CargoArmCommand() {
        cargoArm = Robot.cargoArm;
        oi = OI.getInstance();

        addRequirements(cargoArm);
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
    public void end(boolean interrupted) {
        
    }
}

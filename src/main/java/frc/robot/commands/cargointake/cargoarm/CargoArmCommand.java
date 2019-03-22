package frc.robot.commands.cargointake.cargoarm;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.OI;
import frc.robot.subsystems.cargointake.CargoArm;

public class CargoArmCommand extends Command {

    private CargoArm cargoArm;
    private OI oi;

    public CargoArmCommand() {
        cargoArm = CargoArm.getInstance();
        oi = OI.getInstance();

        requires(cargoArm);
    }

    @Override
    protected void execute() {
        cargoArm.setSpeed(oi.getCargoArmSpeed());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
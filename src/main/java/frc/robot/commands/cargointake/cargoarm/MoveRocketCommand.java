package frc.robot.commands.cargointake.cargoarm;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.cargointake.CargoArm;;

public class MoveRocketCommand extends InstantCommand {

    private CargoArm cargoArm;

    public MoveRocketCommand() {
        super();
        
        cargoArm = CargoArm.getInstance();
        requires(cargoArm);
    }

    @Override
    protected void initialize() {
        cargoArm.moveRocket();
    }
}

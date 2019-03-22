package frc.robot.commands.cargointake.cargoarm;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.cargointake.CargoArm;;

public class MoveRocketWaitCommand extends Command {

    private CargoArm cargoArm;

    public MoveRocketWaitCommand() {
        super();
        
        cargoArm = CargoArm.getInstance();
        requires(cargoArm);
    }

    @Override
    protected void initialize() {
        cargoArm.moveRocket();
    }

    @Override
    protected void execute() {
        
    }

    @Override
    protected boolean isFinished() {
        return cargoArm.getState() == CargoArm.State.MANUAL;
    }
}

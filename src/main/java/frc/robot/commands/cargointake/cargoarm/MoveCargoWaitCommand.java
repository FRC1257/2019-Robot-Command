package frc.robot.commands.cargointake.cargoarm;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.cargointake.CargoArm;;

public class MoveCargoWaitCommand extends Command {

    private CargoArm cargoArm;

    public MoveCargoWaitCommand() {
        super();
        
        cargoArm = CargoArm.getInstance();
        requires(cargoArm);
    }

    @Override
    protected void initialize() {
        cargoArm.moveCargo();
    }

    @Override
    protected void execute() {
        
    }

    @Override
    protected boolean isFinished() {
        return cargoArm.getState() == CargoArm.State.MANUAL;
    }
}

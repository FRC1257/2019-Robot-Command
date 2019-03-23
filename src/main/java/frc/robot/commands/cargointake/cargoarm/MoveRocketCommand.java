package frc.robot.commands.cargointake.cargoarm;

import edu.wpi.first.wpilibj.command.InstantCommand;

import frc.robot.Robot;
import frc.robot.subsystems.cargointake.CargoArm;;

public class MoveRocketCommand extends InstantCommand {

    private CargoArm cargoArm;

    public MoveRocketCommand() {
        super();
        
        cargoArm = Robot.cargoArm;
        requires(cargoArm);
    }

    @Override
    protected void initialize() {
        cargoArm.moveRocket();
    }
}

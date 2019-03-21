package frc.robot.commands.intakearm;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.IntakeArm;;

public class MoveRocketCommand extends InstantCommand {

    private IntakeArm intakeArm;

    public MoveRocketCommand() {
        super();
        
        intakeArm = IntakeArm.getInstance();
        requires(intakeArm);
    }

    @Override
    protected void initialize() {
        intakeArm.moveRocket();
    }
}

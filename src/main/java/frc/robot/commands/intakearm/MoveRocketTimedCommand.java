package frc.robot.commands.intakearm;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.IntakeArm;;

public class MoveRocketTimedCommand extends Command {

    private IntakeArm intakeArm;

    public MoveRocketTimedCommand() {
        super();
        
        intakeArm = IntakeArm.getInstance();
        requires(intakeArm);
    }

    @Override
    protected void initialize() {
        intakeArm.moveRocket();
    }

    @Override
    protected void execute() {
        
    }

    @Override
    protected boolean isFinished() {
        return intakeArm.getState() == IntakeArm.State.MANUAL;
    }
}

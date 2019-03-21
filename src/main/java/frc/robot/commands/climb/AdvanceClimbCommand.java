package frc.robot.commands.climb;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.Climb;

public class AdvanceClimbCommand extends InstantCommand {

    private Climb climb;

    public AdvanceClimbCommand() {
        super();
        
        climb = Climb.getInstance();
        requires(climb);
    }

    @Override
    protected void initialize() {
        climb.advanceClimb();
    }
}

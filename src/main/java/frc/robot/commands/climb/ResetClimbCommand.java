package frc.robot.commands.climb;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.Climb;

public class ResetClimbCommand extends InstantCommand {

    private Climb climb;

    public ResetClimbCommand() {
        super();
        
        climb = Climb.getInstance();
        requires(climb);
    }

    @Override
    protected void initialize() {
        climb.reset();
    }
}

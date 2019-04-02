package frc.robot.commands.climb;

import edu.wpi.first.wpilibj.command.InstantCommand;

import frc.robot.Robot;
import frc.robot.subsystems.Climb;

public class AdvanceSecondaryClimbCommand extends InstantCommand {

    private Climb climb;

    public AdvanceSecondaryClimbCommand() {
        super();
        
        climb = Robot.climb;
        requires(climb);
    }

    @Override
    protected void initialize() {
        climb.advanceSecondaryClimb();
    }
}

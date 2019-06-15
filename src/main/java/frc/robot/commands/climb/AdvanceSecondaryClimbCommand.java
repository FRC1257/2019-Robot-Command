package frc.robot.commands.climb;

import edu.wpi.first.wpilibj.command.InstantCommand;

import frc.robot.Robot;
import frc.robot.subsystems.Climb;

/**
 * Advance the Level 2 Hab climb
 */

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

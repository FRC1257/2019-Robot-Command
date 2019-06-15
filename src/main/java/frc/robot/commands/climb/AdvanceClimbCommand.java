package frc.robot.commands.climb;

import edu.wpi.first.wpilibj.command.InstantCommand;

import frc.robot.Robot;
import frc.robot.subsystems.Climb;

/**
 * Advance the Level 3 Hab climb
 */

public class AdvanceClimbCommand extends InstantCommand {

    private Climb climb;

    public AdvanceClimbCommand() {
        super();

        climb = Robot.climb;
        requires(climb);
    }

    @Override
    public void initialize() {
        climb.advanceClimb();
    }

    @Override
    public void end() {

    }

    @Override
    public void interrupted() {
        end();
    }
}

package frc.robot.commands.climb;

import edu.wpi.first.wpilibj.command.InstantCommand;

import frc.robot.Robot;
import frc.robot.subsystems.Climb;

/**
 * Go back a stage in the Level 3 Hab climb
 */

public class BackClimbCommand extends InstantCommand {

    private Climb climb;

    public BackClimbCommand() {
        super();

        climb = Robot.climb;
        requires(climb);
    }

    @Override
    public void initialize() {
        climb.backClimb();
    }

    @Override
    public void end() {

    }

    @Override
    public void interrupted() {
        end();
    }
}

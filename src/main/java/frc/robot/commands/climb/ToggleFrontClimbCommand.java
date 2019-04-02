package frc.robot.commands.climb;

import edu.wpi.first.wpilibj.command.InstantCommand;

import frc.robot.Robot;
import frc.robot.subsystems.Climb;

public class ToggleFrontClimbCommand extends InstantCommand {

    private Climb climb;

    public ToggleFrontClimbCommand() {
        super();
        
        climb = Robot.climb;
        requires(climb);
    }

    @Override
    public void initialize() {
        climb.toggleFront();
    }

    @Override
    public void end() {
        
    }

    @Override
    public void interrupted() {
        end();
    }
}

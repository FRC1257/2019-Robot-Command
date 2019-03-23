package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.InstantCommand;

import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain;

public class TurnLeftCommand extends InstantCommand {

    private Drivetrain drivetrain;

    public TurnLeftCommand() {
        super();
        
        drivetrain = Robot.drivetrain;
        requires(drivetrain);
    }

    @Override
    protected void initialize() {
        drivetrain.turnLeft();
    }
}

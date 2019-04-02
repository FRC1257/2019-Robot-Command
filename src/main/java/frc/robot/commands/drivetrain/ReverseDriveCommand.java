package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.InstantCommand;

import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain;

public class ReverseDriveCommand extends InstantCommand {

    private Drivetrain drivetrain;

    public ReverseDriveCommand() {
        super();
        
        drivetrain = Robot.drivetrain;
        requires(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.toggleReverse();
    }

    @Override
    public void end() {
        
    }

    @Override
    public void interrupted() {
        end();
    }
}

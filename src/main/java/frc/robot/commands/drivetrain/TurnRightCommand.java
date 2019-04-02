package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.InstantCommand;

import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain;

public class TurnRightCommand extends InstantCommand {

    private Drivetrain drivetrain;

    public TurnRightCommand() {
        super();
        
        drivetrain = Robot.drivetrain;
        requires(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.turnRight();
    }

    @Override
    public void end() {
        drivetrain.endTurn();
    }

    @Override
    public void interrupted() {
        end();
    }
}

package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Drivetrain;

public class TurnLeftWaitCommand extends Command {
    
    private Drivetrain drivetrain;

    public TurnLeftWaitCommand() {
        super();
        
        drivetrain = Drivetrain.getInstance();
        requires(drivetrain);
    }

    @Override
    protected void initialize() {
        drivetrain.turnLeft();
    }

    @Override
    protected void execute() {
        
    }

    @Override
    protected boolean isFinished() {
        return drivetrain.getState() == Drivetrain.State.DRIVER;
    }
}
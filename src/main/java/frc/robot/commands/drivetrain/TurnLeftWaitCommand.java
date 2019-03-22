package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

import frc.robot.RobotMap;
import frc.robot.subsystems.Drivetrain;

public class TurnLeftWaitCommand extends Command {
    
    private Drivetrain drivetrain;

    private double start;

    public TurnLeftWaitCommand() {
        super();
        
        drivetrain = Drivetrain.getInstance();
        requires(drivetrain);
    }

    @Override
    protected void initialize() {
        drivetrain.turnLeft();
        start = Timer.getFPGATimestamp();
    }

    @Override
    protected void execute() {
        
    }

    @Override
    protected boolean isFinished() {
        return drivetrain.getState() == Drivetrain.State.DRIVER || 
        Timer.getFPGATimestamp() - start > RobotMap.DRIVE_TURN_PID_WAIT;
    }
}
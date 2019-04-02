package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.subsystems.Drivetrain;

public class TurnRightWaitCommand extends Command {
    
    private Drivetrain drivetrain;

    private double start;

    public TurnRightWaitCommand() {
        super();
        
        drivetrain = Robot.drivetrain;
        requires(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.turnRight();
        start = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        
    }

    @Override
    public boolean isFinished() {
        return drivetrain.getState() == Drivetrain.State.DRIVER || 
            Timer.getFPGATimestamp() - start > RobotMap.DRIVE_TURN_PID_WAIT;
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
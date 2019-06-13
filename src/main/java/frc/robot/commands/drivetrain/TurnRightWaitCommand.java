package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain;

/**
 * Begin turning right with the drivetrain
 * Once this begins, the driver WILL NOT be able to cancel it through controller input
 *
 * For use in autonomous routines
 */

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
            Timer.getFPGATimestamp() - start > Drivetrain.DRIVE_TURN_PID_WAIT;
    }

    @Override
    public void end() {

    }

    @Override
    public void interrupted() {
        end();
    }
}
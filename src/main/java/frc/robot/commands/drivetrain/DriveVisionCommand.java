package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

/**
 * Set the speed of the drivetrain with the values from the controller and update them with vision
 *
 * Unused for now, needs testing
 */

public class DriveVisionCommand extends Command {

    private Drivetrain drivetrain;
    private Vision vision;

    public DriveVisionCommand() {
        drivetrain = Robot.drivetrain;
        vision = Robot.vision;

        requires(drivetrain);
    }

    @Override
    public void execute() {
        drivetrain.drive(vision.getForwardSpeed(), vision.getTurnSpeed());
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end() {
        
    }

    @Override
    public void interrupted() {
        end();
    }
}
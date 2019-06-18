package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.OI;
import frc.robot.subsystems.Drivetrain;

/**
 * Set the speed of the drivetrain with the values from the controller
 *
 * Default command of the Drivetrain subsystem
 */

public class DriveCommand extends Command {

    private Drivetrain drivetrain;
    private OI oi;

    public DriveCommand() {
        drivetrain = Robot.drivetrain;
        oi = OI.getInstance();

        requires(drivetrain);
    }

    @Override
    public void execute() {
        drivetrain.drive(oi.getDriveForwardSpeed(), oi.getDriveTurnSpeed());
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

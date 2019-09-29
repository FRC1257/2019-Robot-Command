package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.OI;
import frc.robot.subsystems.Drivetrain;

/**
 * Set the speed of the drivetrain with the values from the controller
 *
 * Default command of the Drivetrain subsystem
 */

public class DriveCommand extends CommandBase {

    private Drivetrain drivetrain;
    private OI oi;

    public DriveCommand() {
        drivetrain = Robot.drivetrain;
        oi = OI.getInstance();

        addRequirements(drivetrain);
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
    public void end(boolean interrupted) {
        
    }
}

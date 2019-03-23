package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.Robot;
import frc.robot.OI;
import frc.robot.subsystems.Drivetrain;

public class DriveCommand extends Command {

    private Drivetrain drivetrain;
    private OI oi;

    public DriveCommand() {
        drivetrain = Robot.drivetrain;
        oi = OI.getInstance();

        requires(drivetrain);
    }

    @Override
    protected void execute() {
        drivetrain.drive(oi.getDriveForwardSpeed(), oi.getDriveTurnSpeed());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
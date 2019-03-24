package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class DriveVisionCommand extends Command {

    private Drivetrain drivetrain;
    private Vision vision;

    public DriveVisionCommand() {
        drivetrain = Robot.drivetrain;
        vision = Vision.getInstance();

        requires(drivetrain);
    }

    @Override
    protected void execute() {
        drivetrain.drive(vision.getForwardSpeed(), vision.getTurnSpeed());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
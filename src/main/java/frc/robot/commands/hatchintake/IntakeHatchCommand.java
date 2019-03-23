package frc.robot.commands.hatchintake;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.Robot;
import frc.robot.subsystems.HatchIntake;

public class IntakeHatchCommand extends Command {

    private HatchIntake hatchIntake;

    public IntakeHatchCommand() {
        hatchIntake = Robot.hatchIntake;

        requires(hatchIntake);
    }

    @Override
    protected void execute() {
        hatchIntake.intake();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
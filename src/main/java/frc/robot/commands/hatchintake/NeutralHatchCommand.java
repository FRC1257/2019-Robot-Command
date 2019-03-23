package frc.robot.commands.hatchintake;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.Robot;
import frc.robot.subsystems.HatchIntake;

public class NeutralHatchCommand extends Command {

    private HatchIntake hatchIntake;

    public NeutralHatchCommand() {
        hatchIntake = Robot.hatchIntake;

        requires(hatchIntake);
    }

    @Override
    protected void execute() {
        hatchIntake.neutral();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
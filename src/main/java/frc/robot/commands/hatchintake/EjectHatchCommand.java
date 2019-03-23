package frc.robot.commands.hatchintake;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.Robot;
import frc.robot.subsystems.HatchIntake;

public class EjectHatchCommand extends Command {

    private HatchIntake hatchIntake;

    public EjectHatchCommand() {
        hatchIntake = Robot.hatchIntake;

        requires(hatchIntake);
    }

    @Override
    protected void execute() {
        hatchIntake.eject();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
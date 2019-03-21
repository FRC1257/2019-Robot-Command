package frc.robot.commands.hatchintake.hatchsolenoids;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.subsystems.hatchintake.HatchSolenoids;

public class NeutralHatchCommand extends Command {

    private HatchSolenoids hatchSolenoids;

    public NeutralHatchCommand() {
        hatchSolenoids = HatchSolenoids.getInstance();

        requires(hatchSolenoids);
    }

    @Override
    protected void execute() {
        hatchSolenoids.neutral();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
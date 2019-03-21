package frc.robot.commands.hatchintake.hatchsolenoids;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.subsystems.hatchintake.HatchSolenoids;

public class EjectHatchCommand extends Command {

    private HatchSolenoids hatchSolenoids;

    public EjectHatchCommand() {
        hatchSolenoids = HatchSolenoids.getInstance();

        requires(hatchSolenoids);
    }

    @Override
    protected void execute() {
        hatchSolenoids.eject();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
package frc.robot.commands.hatchintake.hatchsolenoids;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.subsystems.hatchintake.HatchSolenoids;

public class PickupHatchCommand extends Command {

    private HatchSolenoids hatchSolenoids;

    public PickupHatchCommand() {
        hatchSolenoids = HatchSolenoids.getInstance();

        requires(hatchSolenoids);
    }

    @Override
    protected void execute() {
        hatchSolenoids.pickup();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
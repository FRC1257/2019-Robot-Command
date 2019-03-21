package frc.robot.commands.hatchintake.hatchpivot;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.OI;
import frc.robot.subsystems.hatchintake.HatchPivot;

public class HatchPivotCommand extends Command {

    private HatchPivot hatchPivot;
    private OI oi;

    public HatchPivotCommand() {
        hatchPivot = HatchPivot.getInstance();
        oi = OI.getInstance();

        requires(hatchPivot);
    }

    @Override
    protected void execute() {
        hatchPivot.setSpeed(oi.getHatchPivotSpeed());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
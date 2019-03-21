package frc.robot.commands.hatchintake.hatchpivot;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.hatchintake.HatchPivot;

public class LowerPivotTimedCommand extends Command {

    private HatchPivot hatchPivot;

    public LowerPivotTimedCommand() {
        super();
        
        hatchPivot = HatchPivot.getInstance();
        requires(hatchPivot);
    }

    @Override
    protected void initialize() {
        hatchPivot.lowerPivot();
    }

    @Override
    protected void execute() {
        
    }

    @Override
    protected boolean isFinished() {
        return hatchPivot.getState() == HatchPivot.State.MANUAL;
    }
}

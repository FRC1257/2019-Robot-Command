package frc.robot.commands.hatchintake.hatchpivot;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.hatchintake.HatchPivot;

public class RaisePivotCommand extends InstantCommand {

    private HatchPivot hatchPivot;

    public RaisePivotCommand() {
        super();
        
        hatchPivot = HatchPivot.getInstance();
        requires(hatchPivot);
    }

    @Override
    protected void initialize() {
        hatchPivot.raisePivot();
    }
}

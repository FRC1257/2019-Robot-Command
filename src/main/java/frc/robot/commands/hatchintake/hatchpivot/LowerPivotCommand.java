package frc.robot.commands.hatchintake.hatchpivot;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.hatchintake.HatchPivot;

public class LowerPivotCommand extends InstantCommand {

    private HatchPivot hatchPivot;

    public LowerPivotCommand() {
        super();
        
        hatchPivot = HatchPivot.getInstance();
        requires(hatchPivot);
    }

    @Override
    protected void initialize() {
        hatchPivot.lowerPivot();
    }
}

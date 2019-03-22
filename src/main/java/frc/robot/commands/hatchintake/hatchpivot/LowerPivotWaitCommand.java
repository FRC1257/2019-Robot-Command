package frc.robot.commands.hatchintake.hatchpivot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

import frc.robot.RobotMap;
import frc.robot.subsystems.hatchintake.HatchPivot;

public class LowerPivotWaitCommand extends Command {

    private HatchPivot hatchPivot;

    private double start;

    public LowerPivotWaitCommand() {
        super();
        
        hatchPivot = HatchPivot.getInstance();
        requires(hatchPivot);
    }

    @Override
    protected void initialize() {
        hatchPivot.lowerPivot();
        start = Timer.getFPGATimestamp();
    }

    @Override
    protected void execute() {
        
    }

    @Override
    protected boolean isFinished() {
        return hatchPivot.getState() == HatchPivot.State.MANUAL || 
        Timer.getFPGATimestamp() - start > RobotMap.HATCH_PIVOT_PID_WAIT;
    }
}

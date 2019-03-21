package frc.robot.commands.intakearm;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.IntakeArm;;

public class MoveCargoCommand extends InstantCommand {

    private IntakeArm intakeArm;

    public MoveCargoCommand() {
        super();
        
        intakeArm = IntakeArm.getInstance();
        requires(intakeArm);
    }

    @Override
    protected void initialize() {
        intakeArm.moveCargo();
    }
}

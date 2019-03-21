package frc.robot.commands.intakearm;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.OI;
import frc.robot.subsystems.IntakeArm;

public class IntakeArmCommand extends Command {

    private IntakeArm intakeArm;
    private OI oi;

    public IntakeArmCommand() {
        intakeArm = IntakeArm.getInstance();
        oi = OI.getInstance();

        requires(intakeArm);
    }

    @Override
    protected void execute() {
        intakeArm.setSpeed(oi.getIntakeArmSpeed());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
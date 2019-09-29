package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystems.HatchIntake;

/**
 * Constantly intake hatch panel in order to retain it
 * 
 * In the alternate control scheme, this will use a joystick to control the intake
 *
 * Default command of HatchIntake subsystem
 */

public class NeutralHatchCommand extends CommandBase {

    private HatchIntake hatchIntake;
    private OI oi;

    public NeutralHatchCommand() {
        hatchIntake = Robot.hatchIntake;
        oi = OI.getInstance();

        addRequirements(hatchIntake);
    }

    @Override
    public void execute() {
        switch (oi.controlScheme) {
            case ORIGINAL:
                hatchIntake.neutral();
                break;
            case MODIFIED:
                if (oi.getHatchIntakeSpeed() < -0.2) {
                    hatchIntake.intake();
                } else if (oi.getHatchIntakeSpeed() > 0.2) {
                    hatchIntake.eject();
                } else {
                    hatchIntake.neutral();
                }
                break;
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {

    }
}

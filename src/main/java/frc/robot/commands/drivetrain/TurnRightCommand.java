package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.Drivetrain;

public class TurnRightCommand extends InstantCommand {

    private Drivetrain drivetrain;

    public TurnRightCommand() {
        super();
        
        drivetrain = Drivetrain.getInstance();
        requires(drivetrain);
    }

    @Override
    protected void initialize() {
        drivetrain.turnRight();
    }
}

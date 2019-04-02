package frc.robot.util;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * @author FRC Team 1923
 */

public class CGUtils {
    
    public static CommandGroup sequential(Command... commands) {
        CommandGroup commandGroup = new CommandGroup();

        for (Command command : commands) {
            commandGroup.addSequential(command);
        }

        return commandGroup;
    }

    public static CommandGroup parallel(Command... commands) {
        CommandGroup commandGroup = new CommandGroup();

        for (Command command : commands) {
            commandGroup.addParallel(command);
        }

        return commandGroup;
    }

}
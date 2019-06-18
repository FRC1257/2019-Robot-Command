package frc.robot.util;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Vector;
import java.util.function.BooleanSupplier;

/**
 * Command that runs once a certain boolean condition is met
 * 
 * @author FRC Team 1923
 */

public class QueueCommand extends Command {

    private Command command;
    private BooleanSupplier conditional;

    private boolean executed;

    public QueueCommand(Command command, BooleanSupplier conditional) {
        if (command == null) {
            throw new NullPointerException("Command cannot be null.");
        }

        this.command = command;
        this.conditional = conditional;

        try {
            Field requirementsField = Command.class.getDeclaredField("m_requirements");
            requirementsField.setAccessible(true);

            Object requirements = requirementsField.get(command);

            Field setField = requirements.getClass().getDeclaredField("m_set");
            setField.setAccessible(true);

            @SuppressWarnings("unchecked")
            Vector<Object> subsystems = (Vector<Object>) setField.get(requirements);

            for (Object subsystem : subsystems) {
                this.requires((Subsystem) subsystem);
            }
        } catch (Exception e) {
            e.printStackTrace();

            throw new RuntimeException(
                    "Cannot retrieve " + command.getClass().getName() + "'s dependencies.");
        }
    }

    @Override
    public void initialize() {
        this.executed = false;
    }

    @Override
    public void execute() {
        if (this.conditional.getAsBoolean() && !this.executed) {
            try {
                Method clearRequirements = Command.class.getDeclaredMethod("clearRequirements");

                clearRequirements.setAccessible(true);
                clearRequirements.invoke(this.command);
            } catch (Exception e) {
                e.printStackTrace();

                throw new RuntimeException(
                        "Cannot clear " + this.command.getClass().getName() + "'s requirements.");
            }

            this.command.start();
            this.executed = true;
        }
    }

    @Override
    public boolean isFinished() {
        return this.executed && !this.command.isRunning();
    }

    @Override
    public void end() {
        if (this.command.isRunning()) {
            this.command.cancel();
        }
    }

    @Override
    public void interrupted() {
        this.end();
    }
}

package frc.robot.util;

import edu.wpi.first.wpilibj.buttons.Trigger;

public class SnailControllerTrigger extends Trigger {

    /** 
     * Xbox controller utility class for calling commands upon the trigger being pressed
     */

    private SnailController snailController;
    private boolean right;

    public SnailControllerTrigger(SnailController snailController, boolean right) {
        this.snailController = snailController;
        this.right = right;
    }

    @Override
    public boolean get() {
        if(right) {
            return snailController.getRightTrigger() > 0.5;
        }
        else {
            return snailController.getLeftTrigger() > 0.5;
        }
    }
}
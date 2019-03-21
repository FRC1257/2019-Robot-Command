package frc.robot.util;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import frc.robot.RobotMap;

public class SnailController extends Joystick {

    /** 
     * Xbox controller for command based programming
     * Credit to FRC Team 319 for the original code
     */
    
	public JoystickButton aButton = new JoystickButton(this, 1);
	public JoystickButton bButton = new JoystickButton(this, 2);
	public JoystickButton xButton = new JoystickButton(this, 3);
	public JoystickButton yButton = new JoystickButton(this, 4);
	public JoystickButton leftBumper = new JoystickButton(this, 5);
	public JoystickButton rightBumper = new JoystickButton(this, 6);
	public JoystickButton selectButton = new JoystickButton(this, 7);
	public JoystickButton startButton = new JoystickButton(this, 8);
	public JoystickButton leftStickButton = new JoystickButton(this, 9);
	public JoystickButton rightStickButton = new JoystickButton(this, 10);
	public SnailControllerTrigger rightTrigger = new SnailControllerTrigger(this, true);
	public SnailControllerTrigger leftTrigger = new SnailControllerTrigger(this, false);

	public SnailController(int port) {
        super(port);
    }

    public double getLeftStickX() {
        return applyDeadband(getRawAxis(0));
    }
    public double getLeftStickY() {
        return applyDeadband(-getRawAxis(1));
    }
    public double getRightStickX() {
        return applyDeadband(getRawAxis(4));
    }
    public double getRightStickY() {
        return applyDeadband(getRawAxis(5));
    }

    public double getLeftTrigger() {
        return applyDeadband(getRawAxis(2));
    }
    public double getRightTrigger() {
        return applyDeadband(-getRawAxis(3));
	}
	
	/*
	 * Controls: If they press A, use single stick arcade with the left joystick
	 * 
	 * If they press the left bumper, use the left joystick for forward and backward
	 * motion and the right joystick for turning
	 * 
	 * If they press the right bumper, use the right joystick for forward and
	 * backward motion and the left joystick for turning
	 */

	public double getForwardSpeed() {
		if (aButton.get())
			return getY(Hand.kLeft);
		else if (leftBumper.get())
			return getY(Hand.kLeft);
		else if (rightBumper.get())
			return getY(Hand.kRight);
		else
			return 0;
	}

	public double getTurnSpeed() {
		if (aButton.get())
			return getX(Hand.kLeft);
		else if (leftBumper.get())
			return getX(Hand.kRight);
		else if (rightBumper.get())
			return getX(Hand.kLeft);
		else
			return 0;
	}

	public double applyDeadband(double number) {
		if (Math.abs(number) < RobotMap.CONTROLLER_DEADBAND) {
			return 0;
		}
		return number;
	}

	public static double squareInput(double number) {
		// Use abs to prevent the sign from being cancelled out
		return Math.abs(number) * number;
	}
}
package frc.robot;

public class RobotMap {

    /**
     * The RobotMap is a mapping from the ports sensors and actuators are wired into
     * to a variable name. This provides flexibility changing wiring, makes checking
     * the wiring easier and significantly reduces the number of magic numbers
     * floating around. All constants should be ordered by subsystem/use. 
     * The units of each measurement should be specified in a comment
     */

    // General
    public static final int NEO_CURRENT_LIMIT = 50; // amps
    public static final int PCM_SECONDARY_ID = 10;

    // Controllers
    public static final int CONTROLLER_DRIVE_PORT = 0;
    public static final int CONTROLLER_OPERATOR_PORT = 1; 
    public static final double CONTROLLER_DEADBAND = 0.08; // Deadband for the joysticks of the controllers

    // Drive
    public static final int DRIVE_FRONT_LEFT = 4;
    public static final int DRIVE_FRONT_RIGHT = 3;
    public static final int DRIVE_BACK_LEFT = 2;
    public static final int DRIVE_BACK_RIGHT = 1;
    public static final double DRIVE_FORWARD_MAX_SPEED = 1.0;
    public static final double DRIVE_TURN_MAX_SPEED = 0.8;

    public static double[] DRIVE_TURN_PIDF = { 0.01, 0.0, 0.0, 0.0 };
    public static double DRIVE_TURN_TOLERANCE = 3.0;
    public static double DRIVE_TURN_PID_MAX_OUTPUT = 0.8;
    public static double DRIVE_TURN_PID_MIN_OUTPUT = -0.8;

    // Hatch Intake
    public static final int HATCH_PIVOT_MOTOR_ID = 5;
    public static final int HATCH_PICKUP_SOLENOID_ID_FORWARD = 4;
    public static final int HATCH_PICKUP_SOLENOID_ID_REVERSED = 6;
    public static final int HATCH_EJECT_SOLENOID_ID = 5;
    public static final int HATCH_LIMIT_SWITCH_PIVOT_ID = 2;
    public static final int HATCH_LIMIT_SWITCH_HATCH_ID = 0;

    // Hatch Intake: 0 is at top, positive means lower
    public static final double HATCH_PID_LOWERED = 11.0; // Position of the hatch intake when lowered
    public static final double HATCH_PID_RAISED = -0.7; // Position of the hatch intake when raised
    public static double HATCH_MOTOR_MAX_SPEED = 1.0;

    public static double[] HATCH_PIDF = { 0.045, 0.0, 0.0, 0.0 };
    public static final double HATCH_TOLERANCE = 1.0;
    public static final double HATCH_PID_MAX_OUTPUT = 0.8;
    public static final double HATCH_PID_MIN_OUTPUT = -0.8;

    // Intake Arm
    public static final int INTAKE_ARM_MOTOR_ID = 6;
    public static final int INTAKE_ARM_LIMIT_SWITCH_ID = 1;

    // Intake Arm: 0 is at bottom, positive means higher
    public static final double INTAKE_ARM_PID_ROCKET = 11.0; // Target position for rocket
    public static final double INTAKE_ARM_PID_CARGO = 18.5; // Target position for cargo ship
    public static final double INTAKE_ARM_PID_RAISED = 29.5; // Initial position of arm
    public static double INTAKE_ARM_MOTOR_MAX_SPEED = 1.0;

    public static final double[] INTAKE_ARM_PIDF = { 0.1, 0.0, 0.0, 0.0 };
    public static final double INTAKE_ARM_TOLERANCE = 1.0;
    public static final double INTAKE_ARM_PID_MAX_OUTPUT = 1.0;
    public static final double INTAKE_ARM_PID_MIN_OUTPUT = -1.0;

    // Cargo Intake
    public static final int CARGO_INTAKE_MOTOR_ID = 7;
    public static double CARGO_EJECT_SPEED = -0.8; // percentage
    public static double CARGO_INTAKE_SPEED = 1.0; // percentage
    public static double CARGO_CONSTANT_INTAKE_SPEED = 0.2; // percentage

    // Climb
    public static final int CLIMB_FRONT_MOTOR = 9;
    public static final int CLIMB_BACK_MOTOR = 8;

    public static final int CLIMB_FRONT_SOLENOID_FORWARD = 0;
    public static final int CLIMB_FRONT_SOLENOID_REVERSE = 1;
    public static final int CLIMB_BACK_SOLENOID_FORWARD = 2;
    public static final int CLIMB_BACK_SOLENOID_REVERSE = 3;

    public static double CLIMB_MOTOR_MAX_SPEED = 1.0;
    public static final double CLIMB_CRITICAL_ANGLE = 5.0; // Critical angle before the climb stabilizer kicks in
}

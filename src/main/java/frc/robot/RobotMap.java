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
    public static final int DRIVE_FRONT_LEFT_ID = 4;
    public static final int DRIVE_FRONT_RIGHT_ID = 3;
    public static final int DRIVE_BACK_LEFT_ID = 2;
    public static final int DRIVE_BACK_RIGHT_ID = 1;
    public static final double DRIVE_FORWARD_MAX_SPEED = 1.0;
    public static final double DRIVE_TURN_MAX_SPEED = 0.8;

    public static double[] DRIVE_TURN_PIDF = { 0.01, 0.0, 0.0, 0.0 };
    public static double DRIVE_TURN_PID_TOLERANCE = 3.0;
    public static double DRIVE_TURN_PID_WAIT = 2.0;
    public static double DRIVE_TURN_PID_MAX_OUTPUT = 0.8;
    public static double DRIVE_TURN_PID_MIN_OUTPUT = -0.8;

    // Hatch Pivot
    public static final int HATCH_PIVOT_MOTOR_ID = 5;
    public static final int HATCH_PIVOT_LIMIT_SWITCH_ID = 2;

    // Hatch Pivot: 0 is at top, positive means lower
    public static final double HATCH_PIVOT_PID_LOWERED = 11.0; // Position of the hatch intake when lowered
    public static final double HATCH_PIVOT_PID_RAISED = -0.7; // Position of the hatch intake when raised
    public static double HATCH_PIVOT_MAX_SPEED = 1.0;

    public static double[] HATCH_PIVOT_PIDF = { 0.045, 0.0, 0.0, 0.0 };
    public static final double HATCH_PIVOT_PID_TOLERANCE = 1.0;
    public static final double HATCH_PIVOT_PID_WAIT = 2.0;
    public static final double HATCH_PIVOT_PID_MAX_OUTPUT = 0.8;
    public static final double HATCH_PIVOT_PID_MIN_OUTPUT = -0.8;

    // Hatch Solenoids
    public static final int HATCH_SOLENOIDS_PICKUP_FORWARD_ID = 4;
    public static final int HATCH_SOLENOIDS_PICKUP_REVERSE_ID = 6;
    public static final int HATCH_SOLENOIDS_EJECT_ID = 5;
    public static final int HATCH_SOLENOIDS_LIMIT_SWITCH_ID = 0;

    // Cargo Arm
    public static final int CARGO_ARM_MOTOR_ID = 6;
    public static final int CARGO_ARM_LIMIT_SWITCH_ID = 1;

    // Cargo Arm: 0 is at bottom, positive means higher
    public static final double CARGO_ARM_PID_ROCKET = 11.0; // Target position for rocket
    public static final double CARGO_ARM_PID_CARGO = 18.5; // Target position for cargo ship
    public static final double CARGO_ARM_PID_RAISED = 29.5; // Initial position of arm
    public static double CARGO_ARM_MAX_SPEED = 1.0;

    public static final double[] CARGO_ARM_PIDF = { 0.1, 0.0, 0.0, 0.0 };
    public static final double CARGO_ARM_PID_TOLERANCE = 1.0;
    public static final double CARGO_ARM_PID_WAIT = 2.0;
    public static final double CARGO_ARM_PID_MAX_OUTPUT = 1.0;
    public static final double CARGO_ARM_PID_MIN_OUTPUT = -1.0;

    // Cargo Roller
    public static final int CARGO_ROLLER_MOTOR_ID = 7;
    public static double CARGO_ROLLER_EJECT_SPEED = -0.8; // percentage
    public static double CARGO_ROLLER_INTAKE_SPEED = 1.0; // percentage
    public static double CARGO_ROLLER_CONSTANT_INTAKE_SPEED = 0.2; // percentage

    // Climb
    public static final int CLIMB_FRONT_MOTOR_ID = 9;
    public static final int CLIMB_BACK_MOTOR_ID = 8;

    public static final int CLIMB_FRONT_SOLENOID_FORWARD_ID = 0;
    public static final int CLIMB_FRONT_SOLENOID_REVERSE_ID = 1;
    public static final int CLIMB_BACK_SOLENOID_FORWARD_ID = 2;
    public static final int CLIMB_BACK_SOLENOID_REVERSE_ID = 3;

    public static double CLIMB_DRIVE_MAX_SPEED = 1.0;
    public static final double CLIMB_CRITICAL_ANGLE = 5.0; // Critical angle before the climb stabilizer kicks in
}

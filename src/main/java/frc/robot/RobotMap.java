package frc.robot;

/**
 * Contains all of the general robot constants and the electrical layout of the robot
 * All constants should have units specified in a comment next to it
 */

public class RobotMap {

    // Controllers
    public static final int CONTROLLER_DRIVE_PORT = 0;
    public static final int CONTROLLER_OPERATOR_PORT = 1;

    // PCM
    public static final int PCM_SECONDARY_ID = 10;

    // Drivetrain
    public static final int DRIVE_FRONT_LEFT_ID = 4;
    public static final int DRIVE_FRONT_RIGHT_ID = 3;
    public static final int DRIVE_BACK_LEFT_ID = 2;
    public static final int DRIVE_BACK_RIGHT_ID = 1;

    // Hatch Intake
    public static final int HATCH_INTAKE_MOTOR_ID = 5;

    // Cargo Arm
    public static final int CARGO_ARM_MOTOR_ID = 6;
    public static final int CARGO_ARM_LIMIT_SWITCH_ID = 1;

    // Cargo Roller
    public static final int CARGO_ROLLER_MOTOR_ID = 7;

    // Climb
    public static final int CLIMB_FRONT_MOTOR_ID = 9;
    public static final int CLIMB_BACK_MOTOR_ID = 8;
    public static final int CLIMB_FRONT_SOLENOID_FORWARD_ID = 0;
    public static final int CLIMB_FRONT_SOLENOID_REVERSE_ID = 1;
    public static final int CLIMB_BACK_SOLENOID_FORWARD_ID = 2;
    public static final int CLIMB_BACK_SOLENOID_REVERSE_ID = 3;

    // General
    public static final int NEO_CURRENT_LIMIT = 50; // amps
}

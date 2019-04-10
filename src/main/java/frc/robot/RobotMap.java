package frc.robot;

import frc.robot.util.snail_vision.*;

public class RobotMap {

    /*
     * The RobotMap is a mapping from the ports sensors and actuators are wired into
     * to a variable name. This provides flexibility changing wiring, makes checking
     * the wiring easier and significantly reduces the number of magic numbers
     * floating around. All constants should be ordered by subsystem/use. 
     * The units of each measurement should be specified in a comment
     */

    /*
     * Needed Measurements:
     * Drive turn PIDF constants
     * Cargo arm positions and PIDF constants (something changed in the gearbox, not sure what)
     */

    public static class ElectricalLayout {
        public static final int CONTROLLER_DRIVE_PORT = 0;
        public static final int CONTROLLER_OPERATOR_PORT = 1; 

        public static final int PCM_SECONDARY_ID = 10;

        public static final int DRIVE_FRONT_LEFT_ID = 4;
        public static final int DRIVE_FRONT_RIGHT_ID = 3;
        public static final int DRIVE_BACK_LEFT_ID = 2;
        public static final int DRIVE_BACK_RIGHT_ID = 1;

        public static final int HATCH_INTAKE_MOTOR_ID = 5;

        public static final int CARGO_ARM_MOTOR_ID = 6;
        public static final int CARGO_ARM_LIMIT_SWITCH_ID = 1;

        public static final int CARGO_ROLLER_MOTOR_ID = 7;

        public static final int CLIMB_FRONT_MOTOR_ID = 9;
        public static final int CLIMB_BACK_MOTOR_ID = 8;

        public static final int CLIMB_FRONT_SOLENOID_FORWARD_ID = 0;
        public static final int CLIMB_FRONT_SOLENOID_REVERSE_ID = 1;
        public static final int CLIMB_BACK_SOLENOID_FORWARD_ID = 2;
        public static final int CLIMB_BACK_SOLENOID_REVERSE_ID = 3;
    }

    // General
    public static final int NEO_CURRENT_LIMIT = 50; // amps

    // Controllers
    public static final double CONTROLLER_DEADBAND = 0.08; // deadband for joysticks of controllers

    // Drive
    public static final double DRIVE_FORWARD_MAX_SPEED = 1.0; // percentage
    public static final double DRIVE_TURN_MAX_SPEED = 0.8; // percentage

    public static double[] DRIVE_TURN_PIDF = { 0.01, 0.0, 0.0, 0.0 };
    public static double DRIVE_TURN_PID_TOLERANCE = 3.0; // degrees
    public static double DRIVE_TURN_PID_WAIT = 2.0; // seconds
    public static double DRIVE_TURN_PID_MAX_OUTPUT = 0.8; // percentage
    public static double DRIVE_TURN_PID_MIN_OUTPUT = -0.8; // percentage

    // Hatch Intake
    public static double HATCH_INTAKE_EJECT_SPEED = -1.0; // percentage
    public static double HATCH_INTAKE_INTAKE_SPEED = 1.0; // percentage
    public static double HATCH_INTAKE_CONSTANT_INTAKE_SPEED = 0.2; // percentage

    // Cargo Arm
    public static final double CARGO_ARM_PID_ROCKET = 11.0; // Target position for rocket (revolutions)
    public static final double CARGO_ARM_PID_CARGO = 18.5; // Target position for cargo ship (revolutions)
    public static final double CARGO_ARM_PID_RAISED = 28.0; // Initial position of arm (revolutions)
    public static double CARGO_ARM_MAX_SPEED = 1.0;

    public static final double[] CARGO_ARM_PIDF = { 0.1, 0.0, 0.0, 0.0 };
    public static final double CARGO_ARM_ARB_F = 0.0;
    public static final double CARGO_ARM_ANGLE_CONV_FACTOR = 90.0 / CARGO_ARM_PID_RAISED; // conversion factor from motor rev to angle
    public static final double CARGO_ARM_PID_TOLERANCE = 1.0; // revolutions
    public static final double CARGO_ARM_PID_WAIT = 2.0; // seconds
    public static final double CARGO_ARM_PID_MAX_OUTPUT = 1.0; // percentage
    public static final double CARGO_ARM_PID_MIN_OUTPUT = -1.0; // percentage

    // Cargo Roller
    public static double CARGO_ROLLER_EJECT_SPEED = -0.8; // percentage
    public static double CARGO_ROLLER_INTAKE_SPEED = 1.0; // percentage
    public static double CARGO_ROLLER_CONSTANT_INTAKE_SPEED = 0.2; // percentage

    // Climb
    public static double CLIMB_DRIVE_MAX_SPEED = 1.0; // percentage
    public static final double CLIMB_CRITICAL_ANGLE = 5.0; // Critical angle before the climb stabilizer kicks in (degrees)
    
    // Vision
    public static final double[] AREA_TO_DISTANCE_ROCKET = {1};
    public static final double[] AREA_TO_DISTANCE_SHIP = {1};

    public static void initializeVision(SnailVision vision) {
        vision.ANGLE_CORRECT_P = -0.02;
        vision.ANGLE_CORRECT_F = 0.2;
        vision.ANGLE_CORRECT_MIN_ANGLE = 2.0; // degrees
        
        vision.GET_IN_DISTANCE_P = 4.0;
        vision.GET_IN_DISTANCE_ERROR = 3.0; // inches
        vision.DISTANCE_ESTIMATION_METHOD = "area";

        vision.JERK_COLLISION_THRESHOLD = 1;

        vision.TARGETS.add(new Target(0, 12, 60, AREA_TO_DISTANCE_ROCKET)); // Rocket
        vision.TARGETS.add(new Target(0, 12, 60, AREA_TO_DISTANCE_SHIP)); // Cargo Ship
    }
}

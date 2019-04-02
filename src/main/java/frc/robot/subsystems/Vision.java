package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.util.snail_vision.SnailVision;

/**
 * Class to handle all vision functionality
 * Updates a drive speed and a turn speed with vision calculations
 * These can then be utilized by the drivetrain to align to targets
 */

public class Vision {
    
    private static Vision instance = null;

    private SnailVision vision;
    private double forwardSpeed;
    private double turnSpeed;
    private NetworkTable limelightNetworkTable;

    private OI oi;

    private Vision() {
        vision = new SnailVision(true);
        RobotMap.initializeVision(vision);
        forwardSpeed = 0;
        turnSpeed = 0;
        limelightNetworkTable = NetworkTableInstance.getDefault().getTable("limelight");

        oi = Robot.oi;

        setConstantTuning();
    }

    /**
     * Update drive speeds with vision readings
     */
    public void update() {
        // vision.gyroFunctionality();

        forwardSpeed = oi.getDriveForwardSpeed();
        turnSpeed = oi.getDriveTurnSpeed();

        // Utilize vision and a P controller to turn to a target
        if(oi.getTurnCorrect() > 0) {
            if (vision.currentPipeline.get(0) != 0) {
                SnailVision.changePipeline(limelightNetworkTable, 0); // Dual Target
            }
            turnSpeed -= vision.angleCorrect();
        }
        else if (vision.currentPipeline.get(0) != 2) {
            SnailVision.changePipeline(limelightNetworkTable, 2); // Default driver
        }
    }

    /**
     * Gets the current drive speed from the controller and then utilizes vision to drive to targets
     * 
     * @return the current drive speed with vision modification
     */
    public double getForwardSpeed() {
        return forwardSpeed;
    }

    /**
     * Gets the current turn speed from the controller and then utilizes vision to drive to targets
     * 
     * @return the current turn speed with vision modification
     */
    public double getTurnSpeed() {
        return turnSpeed;
    }

    /**
     * Set up SmartDashboard/Shuffleboard for constant tuning
     */
    public void setConstantTuning() {
        SmartDashboard.putNumber("Vision Angle Correct P", vision.ANGLE_CORRECT_P);
        SmartDashboard.putNumber("Vision Angle Correct F", vision.ANGLE_CORRECT_F);
        SmartDashboard.putNumber("Vision Distance Correct P", vision.GET_IN_DISTANCE_P);
    }

    /**
     * Retrieves constant tuning from SmartDashboard/Shuffleboard
     */
    public void getConstantTuning() {
        vision.ANGLE_CORRECT_P = SmartDashboard.getNumber("Vision Angle Correct P", vision.ANGLE_CORRECT_P);
        vision.ANGLE_CORRECT_F = SmartDashboard.getNumber("Vision Angle Correct F", vision.ANGLE_CORRECT_F);
        vision.GET_IN_DISTANCE_P = SmartDashboard.getNumber("Vision Distance Correct P", vision.GET_IN_DISTANCE_P);
    }

    public static Vision getInstance() {
        if (instance == null) {
            instance = new Vision();
        }
        return instance;
    }
}
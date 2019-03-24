package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.util.snail_vision.SnailVision;

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

        oi = OI.getInstance();

        setConstantTuning();
    }

    public void update() {
        // vision.gyroFunctionality();

        forwardSpeed = oi.getDriveForwardSpeed();
        turnSpeed = oi.getDriveTurnSpeed();

        if(oi.getTurnCorrect() > 0) {
            if (vision.currentPipeline.get(0) != 0) {
                SnailVision.changePipeline(NetworkTableInstance.getDefault().getTable("limelight"), 0); // Dual Target
            }
            turnSpeed -= vision.angleCorrect();
        }
        else if (vision.currentPipeline.get(0) != 2) {
            SnailVision.changePipeline(NetworkTableInstance.getDefault().getTable("limelight"), 2); // Default driver
        }
    }

    public double getForwardSpeed() {
        return forwardSpeed;
    }

    public double getTurnSpeed() {
        return turnSpeed;
    }

    public void setConstantTuning() {
        SmartDashboard.putNumber("Vision Angle Correct P", vision.ANGLE_CORRECT_P);
        SmartDashboard.putNumber("Vision Angle Correct F", vision.ANGLE_CORRECT_F);
        SmartDashboard.putNumber("Vision Distance Correct P", vision.GET_IN_DISTANCE_P);
    }

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
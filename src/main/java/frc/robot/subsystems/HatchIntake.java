package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import frc.robot.RobotMap;
import frc.robot.commands.hatchintake.*;

/**
 * Subsystem to handle intaking and ejecting hatch panels
 *  - Utilizes a single motor attached to a rolling intake
 */

public class HatchIntake extends Subsystem {

    private WPI_VictorSPX intakeMotor;

    /**
     * INTAKING - spinning inwards to take in a hatch
     * EJECTING - spinning outwards to eject a hatch
     * NEUTRAL - spinning slowly inwards to prevent a hatch from falling out
     */
    public enum State {
        INTAKING, EJECTING, NEUTRAL
    }
    private State state = State.NEUTRAL;

    public HatchIntake() {
        intakeMotor = new WPI_VictorSPX(RobotMap.HATCH_INTAKE_MOTOR_ID);
        intakeMotor.setNeutralMode(NeutralMode.Brake);

        setConstantTuning();
        reset();
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new NeutralHatchCommand());
    }
    
    private void reset() {
        intakeMotor.set(0.0);
        state = State.NEUTRAL;
    }

    /**
     * Begin intaking a hatch panel
     */
    public void intake() {
        state = State.INTAKING;
    }

    /**
     * Begin ejecting a hatch panel
     */
    public void eject() {
        state = State.EJECTING;
    }

    /**
     * Return to neutral state (constant intaking)
     */
    public void neutral() {
        state = State.NEUTRAL;
    }

    /**
     * Update motor outputs according to the current state
     */
    public void update() {
        switch(state) {
            case INTAKING:
                intakeMotor.set(RobotMap.HATCH_INTAKE_INTAKE_SPEED);
            break;
            case EJECTING:
                intakeMotor.set(RobotMap.HATCH_INTAKE_EJECT_SPEED);
            break;
            case NEUTRAL:
                intakeMotor.set(RobotMap.HATCH_INTAKE_CONSTANT_INTAKE_SPEED);
            break;
        }
    }

    /**
     * Set up SmartDashboard/Shuffleboard for constant tuning
     */
    private void setConstantTuning() {
        SmartDashboard.putNumber("Hatch Intake Speed", RobotMap.HATCH_INTAKE_INTAKE_SPEED);
        SmartDashboard.putNumber("Hatch Eject Speed", RobotMap.HATCH_INTAKE_EJECT_SPEED);
        SmartDashboard.putNumber("Hatch Constant Speed", RobotMap.HATCH_INTAKE_CONSTANT_INTAKE_SPEED);
    }

    /**
     * Retrieves constant tuning from SmartDashboard/Shuffleboard
     */
    public void getConstantTuning() {
        RobotMap.CARGO_ROLLER_INTAKE_SPEED = SmartDashboard.getNumber("Hatch Intake Speed", RobotMap.HATCH_INTAKE_INTAKE_SPEED);
        RobotMap.CARGO_ROLLER_EJECT_SPEED = SmartDashboard.getNumber("Hatch Eject Speed", RobotMap.HATCH_INTAKE_EJECT_SPEED);
        RobotMap.HATCH_INTAKE_CONSTANT_INTAKE_SPEED = SmartDashboard.getNumber("Hatch Constant Speed", RobotMap.HATCH_INTAKE_CONSTANT_INTAKE_SPEED);
    }

    public State getState() {
        return state;
    }
}
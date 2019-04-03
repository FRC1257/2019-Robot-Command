package frc.robot.subsystems.cargointake;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import frc.robot.RobotMap;
import frc.robot.commands.cargointake.cargoroller.*;

/**
 * Subsystem to handle intaking and ejecting cargo balls
 *  - Utilizes a single motor attached to a rolling intake
 */

public class CargoRoller extends Subsystem {

    private WPI_VictorSPX intakeMotor;

    /**
     * INTAKING - spinning inwards to take in a cargo
     * EJECTING - spinning outwards to eject a cargo
     * NEUTRAL - spinning slowly inwards to prevent a cargo from falling out
     */
    public static enum State {
        INTAKING, EJECTING, NEUTRAL
    }
    private State state = State.NEUTRAL;

    public CargoRoller() {
        intakeMotor = new WPI_VictorSPX(RobotMap.CARGO_ROLLER_MOTOR_ID);
        intakeMotor.setNeutralMode(NeutralMode.Brake);

        setConstantTuning();
        reset();
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new NeutralCargoCommand());
    }
    
    public void reset() {
        intakeMotor.set(0.0);
        state = State.NEUTRAL;
    }

    /**
     * Begin intaking a cargo ball
     */
    public void intake() {
        state = State.INTAKING;
    }

    /**
     * Begin ejecting a cargo ball
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
                intakeMotor.set(RobotMap.CARGO_ROLLER_INTAKE_SPEED);
            break;
            case EJECTING:
                intakeMotor.set(RobotMap.CARGO_ROLLER_EJECT_SPEED);
            break;
            case NEUTRAL:
                intakeMotor.set(RobotMap.CARGO_ROLLER_CONSTANT_INTAKE_SPEED);
            break;
        }
    }

    /**
     * Set up SmartDashboard/Shuffleboard for constant tuning
     */
    public void setConstantTuning() {
        SmartDashboard.putNumber("Cargo Intake Speed", RobotMap.CARGO_ROLLER_INTAKE_SPEED);
        SmartDashboard.putNumber("Cargo Eject Speed", RobotMap.CARGO_ROLLER_EJECT_SPEED);
        SmartDashboard.putNumber("Cargo Constant Speed", RobotMap.CARGO_ROLLER_CONSTANT_INTAKE_SPEED);
    }

    /**
     * Retrieves constant tuning from SmartDashboard/Shuffleboard
     */
    public void getConstantTuning() {
        RobotMap.CARGO_ROLLER_INTAKE_SPEED = SmartDashboard.getNumber("Cargo Intake Speed", RobotMap.CARGO_ROLLER_INTAKE_SPEED);
        RobotMap.CARGO_ROLLER_EJECT_SPEED = SmartDashboard.getNumber("Cargo Eject Speed", RobotMap.CARGO_ROLLER_EJECT_SPEED);
        RobotMap.CARGO_ROLLER_CONSTANT_INTAKE_SPEED = SmartDashboard.getNumber("Cargo Constant Speed", RobotMap.CARGO_ROLLER_CONSTANT_INTAKE_SPEED);
    }

    public State getState() {
        return state;
    }
}
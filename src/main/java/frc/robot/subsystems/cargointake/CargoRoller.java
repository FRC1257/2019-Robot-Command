package frc.robot.subsystems.cargointake;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import frc.robot.RobotMap;
import frc.robot.commands.cargointake.cargoroller.*;

public class CargoRoller extends Subsystem {

    // Constants
    public static double CARGO_ROLLER_EJECT_SPEED = -0.8; // percentage
    public static double CARGO_ROLLER_INTAKE_SPEED = 1.0; // percentage
    public static double CARGO_ROLLER_CONSTANT_INTAKE_SPEED = 0.2; // percentage

    private WPI_VictorSPX intakeMotor;

    public enum State {
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

    private void reset() {
        intakeMotor.set(0.0);
        state = State.NEUTRAL;
    }

    public void intake() {
        state = State.INTAKING;
    }

    public void eject() {
        state = State.EJECTING;
    }

    public void neutral() {
        state = State.NEUTRAL;
    }

    public void update() {
        switch(state) {
            case INTAKING:
                intakeMotor.set(CARGO_ROLLER_INTAKE_SPEED);
            break;
            case EJECTING:
                intakeMotor.set(CARGO_ROLLER_EJECT_SPEED);
            break;
            case NEUTRAL:
                intakeMotor.set(CARGO_ROLLER_CONSTANT_INTAKE_SPEED);
            break;
        }
    }

    private void setConstantTuning() {
        SmartDashboard.putNumber("Cargo Intake Speed", CARGO_ROLLER_INTAKE_SPEED);
        SmartDashboard.putNumber("Cargo Eject Speed", CARGO_ROLLER_EJECT_SPEED);
        SmartDashboard.putNumber("Cargo Constant Speed", CARGO_ROLLER_CONSTANT_INTAKE_SPEED);
    }

    public void getConstantTuning() {
        CARGO_ROLLER_INTAKE_SPEED = SmartDashboard.getNumber("Cargo Intake Speed", CARGO_ROLLER_INTAKE_SPEED);
        CARGO_ROLLER_EJECT_SPEED = SmartDashboard.getNumber("Cargo Eject Speed", CARGO_ROLLER_EJECT_SPEED);
        CARGO_ROLLER_CONSTANT_INTAKE_SPEED = SmartDashboard.getNumber("Cargo Constant Speed", CARGO_ROLLER_CONSTANT_INTAKE_SPEED);
    }

    public State getState() {
        return state;
    }
}
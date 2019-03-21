package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import frc.robot.RobotMap;
import frc.robot.commands.cargointake.*;

public class CargoIntake extends Subsystem {

    private static CargoIntake instance = null;

    private WPI_VictorSPX intakeMotor;

    public static enum State {
        INTAKING, EJECTING, NEUTRAL
    }
    private State state;

    private CargoIntake() {
        intakeMotor = new WPI_VictorSPX(RobotMap.CARGO_INTAKE_MOTOR_ID);
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
                intakeMotor.set(RobotMap.CARGO_INTAKE_SPEED);
            break;
            case EJECTING:
                intakeMotor.set(RobotMap.CARGO_EJECT_SPEED);
            break;
            case NEUTRAL:
                intakeMotor.set(RobotMap.CARGO_CONSTANT_INTAKE_SPEED);
            break;
        }
    }

    public void setConstantTuning() {
        SmartDashboard.putNumber("Cargo Intake Speed", RobotMap.CARGO_INTAKE_SPEED);
        SmartDashboard.putNumber("Cargo Eject Speed", RobotMap.CARGO_EJECT_SPEED);
    }

    public void getConstantTuning() {
        RobotMap.CARGO_INTAKE_SPEED = SmartDashboard.getNumber("Cargo Intake Speed", RobotMap.CARGO_INTAKE_SPEED);
        RobotMap.CARGO_EJECT_SPEED = SmartDashboard.getNumber("Cargo Eject Speed", RobotMap.CARGO_EJECT_SPEED);
    }

    public State getState() {
        return state;
    }

    public static CargoIntake getInstance() {
        if (instance == null) {
            instance = new CargoIntake();
        }
        return instance;
    }
}
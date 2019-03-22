package frc.robot.subsystems.hatchintake;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import frc.robot.RobotMap;
import frc.robot.commands.hatchintake.hatchsolenoids.*;

public class HatchSolenoids extends Subsystem {

    private static HatchSolenoids instance = null;

    private DoubleSolenoid pickupSolenoid;
    private Solenoid ejectSolenoid;

    private DigitalInput limitSwitch;

    public static enum State {
        NEUTRAL, PICKUP, EJECT
    }
    private State state = State.NEUTRAL;

    private HatchSolenoids() {
        pickupSolenoid = new DoubleSolenoid(RobotMap.HATCH_SOLENOIDS_PICKUP_FORWARD_ID, RobotMap.HATCH_SOLENOIDS_PICKUP_REVERSE_ID);
        ejectSolenoid = new Solenoid(RobotMap.HATCH_SOLENOIDS_EJECT_ID);

        limitSwitch = new DigitalInput(RobotMap.HATCH_SOLENOIDS_LIMIT_SWITCH_ID);

        setConstantTuning();
        reset();
    }

    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new NeutralHatchCommand());
    }
    
    public void reset() {
        pickupSolenoid.set(Value.kReverse);
        ejectSolenoid.set(false);
        
        state = State.NEUTRAL;
    }

    public void update() {
        switch(state) {
            case NEUTRAL:
                pickupRetract();
                ejectRetract();
            break;
            case PICKUP:
                pickupExtend();
                ejectRetract();
            break;
            case EJECT:
                pickupRetract();
                ejectExtend();
            break;
        }

        SmartDashboard.putBoolean("Hatch Pickup Extended", getPickupExtended());
        SmartDashboard.putBoolean("Hatch Eject Extended", getEjectExtended());
        SmartDashboard.putBoolean("Hatch Hatch Limit Switch", getLimitSwitch());
    }

    public void setPickup(boolean pickup) {
        if(pickup) {
            pickupExtend();
        }
        else {
            pickupRetract();
        }
    }

    public void setPickup(Value pickup) {
        pickupSolenoid.set(pickup);
    }

    public void pickupExtend() {
        pickupSolenoid.set(Value.kForward);
    }

    public void pickupRetract() {
        pickupSolenoid.set(Value.kReverse);
    }

    public void setEject(boolean eject) {
        ejectSolenoid.set(eject);
    }

    public void ejectExtend() {
        ejectSolenoid.set(true);
    }

    public void ejectRetract() {
        ejectSolenoid.set(false);
    }

    public void neutral() {
        state = State.NEUTRAL;
    }

    public void pickup() {
        state = State.PICKUP;
    }

    public void eject() {
        state = State.EJECT;
    }

    public boolean getPickupExtended() {
        return pickupSolenoid.get() == Value.kForward;
    }

    public boolean getEjectExtended() {
        return ejectSolenoid.get();
    }

    public boolean getLimitSwitch() {
        return !limitSwitch.get();
    }
    
    public void setConstantTuning() {
        
    }

    public void getConstantTuning() {
        
    }
    
    public State getState() {
        return state;
    }

    public static HatchSolenoids getInstance() {
        if (instance == null) {
            instance = new HatchSolenoids();
        }
        return instance;
    }
}

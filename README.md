# 2019-Robot-Command

## Structure

- `Robot.java` - Creates all of the subsystem objects and updates them

- `OI.java` - Binds the operator controls to the commands for the subsystems

- `RobotMap.java` - Contains all constants for the robot code

- `/util` - Utility classes for controllers, PIDF, and Gyro

- `/subsystems` - All subsystems on our robot

- `/commands` - All commands to control the various subsystems

### Subsystems

Each `Subsystem` contains an enum to handle the current state of the subsystem. Each subsystem also contains the function `update()`, which does a `switch` statement on the current state and updates the motor outputs accordingly.

### Commands

Each `Command` updates a single `Subsystem`'s state or internal variables. They don't directly set output to the motor; rather, they update the state of the `Subsystem` and let the `Subsystem`'s `update()` function handle the output to the motors.

Some actions have both a `Command` version and a `WaitCommand` version, specifically the closed loop control modes. The `Command` version is an `InstantCommand` that instantly terminates and brings back operator controller. This allows the operator to be able to instantly interrupt the action if they need to do so. If they don't, the action will continue, but the operator will always have the chance to interrupt. However, the `WaitCommand` version will not instantly terminate and will instead keep away operator control until the action is complete. The `WaitCommand` versions are not used in this iteration of the robot code, but they could be used in the future for sequencing actions during autonomous.

## Credits

- `SynchronousPIDF.java` - FRC Team 254
- `CGUtils.java` + `QueueCommand.java` - FRC Team 1923

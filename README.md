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

## Credits

- `SynchronousPIDF.java` - FRC Team 254
- `CGUtils.java` + `QueueCommand.java` - FRC Team 1923

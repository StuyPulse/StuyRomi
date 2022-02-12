package com.stuypulse.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.stuylib.network.SmartNumber;
import com.stuypulse.stuylib.streams.filters.*;

/**
 * An example command that demonstrates how to 
 * control a drivetrain with gamepad input and filters.
 */
public class DrivetrainDriveForeverCommand extends CommandBase {

    private Drivetrain drivetrain;

    // These filters help smooth out driving but they are also optional
    private IFilter speedFilter = new LowPassFilter(new SmartNumber("Drive Settings/Speed Filter", 0.3));
    private IFilter turnFilter = new LowPassFilter(new SmartNumber("Drive Settings/Turn Filter", 0.2));

    public DrivetrainDriveForeverCommand(Drivetrain subsystem) {
        drivetrain = subsystem;
        
        // This makes sure that two commands that need the same subsystem dont mess eachother up. 
        // Example, if a command activated by a button needs to take control away from a default command.
        addRequirements(subsystem);
    }

    // Called 50 times a second if the robot is running
    @Override
    public void execute() {
        // Get the speed from the triggers
        double speed = 10;

        // Get the turn value from the left stick
        double turn = 0;

        // Filter the Speed and Turn value
        // This is optional, but it leads to a smoother driving experience.
        speed = speedFilter.get(speed);
        turn = turnFilter.get(turn);

        // Send values to drivetrain
        drivetrain.arcadeDrive(speed, turn);
    }
}
package com.stuypulse.robot.commands;

import com.stuypulse.robot.TrajectoryLoader;
import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class DriveDistanceCommand extends SequentialCommandGroup {

    public DriveDistanceCommand(Drivetrain drivetrain, double distance) {
        addCommands(new DrivetrainRamseteCommand(drivetrain, TrajectoryLoader.getLine(distance)));
    }
}

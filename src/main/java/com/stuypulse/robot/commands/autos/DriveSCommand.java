/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.stuypulse.robot.commands.autos;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;

import edu.wpi.first.wpilibj.trajectory.*;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.commands.*;
import com.stuypulse.robot.subsystems.*;

import java.util.List;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class DriveSCommand extends SequentialCommandGroup {

    private static final Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
        // Start at the origin facing the +X direction
        new Pose2d(0, 0, new Rotation2d(0)),
        // Pass through these two interior waypoints, making an 's' curve path
        List.of(
            new Translation2d(0.3, 0.3),
            new Translation2d(0.6, -0.3)
        ),
        // End 3 meters straight ahead of where we started, facing forward
        new Pose2d(0.9, 0, new Rotation2d(0)),
        // Pass config
        Constants.Drivetrain.TrajectoryConfiguration.CONFIG
    );

    public DriveSCommand(Drivetrain drivetrain) {
        // Add your commands in the super() call, e.g.
        // super(new FooCommand(), new BarCommand());
        super(new DrivetrainRamseteCommand(drivetrain, trajectory));
    }
}
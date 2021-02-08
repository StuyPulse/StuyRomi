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
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.*;
import edu.wpi.first.wpilibj.trajectory.constraint.MaxVelocityConstraint;
import edu.wpi.first.wpilibj.trajectory.constraint.RectangularRegionConstraint;
import edu.wpi.first.wpilibj.trajectory.constraint.TrajectoryConstraint;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.FieldMap;
import com.stuypulse.robot.commands.*;
import com.stuypulse.robot.subsystems.*;

import java.util.List;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.util.Units;

// NOTE:  Consider using this command inline, rather than writing a subclass.  
// For more information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html

public class DriveBouncePath1 extends SequentialCommandGroup {

    // maximum values different for every path
    private static final double MAX_VELOCITY = 0.2 / Constants.ROBOT_SCALE; // m/s
    private static final double MAX_ACCELERATION = 0.5 / Constants.ROBOT_SCALE; // m/s^2

    // Trajectory Configuration (ie max velocity, max acceleartion)
    private static final TrajectoryConfig config = new TrajectoryConfig(MAX_VELOCITY, MAX_ACCELERATION);

    // Kinematics used for other constraints
    private static final DifferentialDriveKinematics kinematics = Constants.Drivetrain.Motion.KINEMATICS;

    // List of constraints
    private static final List<TrajectoryConstraint> constraints = List.of();

    // Group 1: c1, c2, b3, a3, b3, d4, e5, d6, a6, d6, e7, e8, d9, c9, a9, c9, c11

    // Trajectory
    private static final Trajectory trajectory = FieldMap.getTrajectory("c1", Rotation2d.fromDegrees(0),

            "c2 b3 a3 b3 d4 e5 d6 a6 d6 e7 e8 d9 c9 a9 c9",

            "c11", Rotation2d.fromDegrees(0),

            config.setKinematics(kinematics).addConstraints(constraints));

    public DriveBouncePath1(Drivetrain drivetrain) {
        // Add your commands in the super() call, e.g.
        // super(new FooCommand(), new BarCommand());
        super(new DrivetrainRamseteCommand(drivetrain, trajectory));
    }
}
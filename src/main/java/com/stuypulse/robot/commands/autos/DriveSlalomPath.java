/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.stuypulse.robot.commands.autos;

import java.util.List;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.FieldMap;
import com.stuypulse.robot.commands.DrivetrainRamseteCommand;
import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.TrajectoryConstraint;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;


// NOTE:  Consider using this command inline, rather than writing a subclass.  
// For more information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html

public class DriveSlalomPath extends SequentialCommandGroup {

    // maximum values different for every path
    private static final double MAX_VELOCITY = 0.25 / Constants.ROBOT_SCALE; // m/s
    private static final double MAX_ACCELERATION = 1.0 / Constants.ROBOT_SCALE; // m/s^2

    // Trajectory Configuration (ie max velocity, max acceleartion)
    private static final TrajectoryConfig config = 
        new TrajectoryConfig(MAX_VELOCITY, MAX_ACCELERATION);

    // Kinematics used for other constraints 
    private static final DifferentialDriveKinematics kinematics = 
        Constants.Drivetrain.Motion.KINEMATICS; 

    // List of constraints
    private static final List<TrajectoryConstraint> constraints = 
        List.of(
        ); 
    
    // Trajectory
    private static final Trajectory trajectory = FieldMap.getTrajectory(
        "e1",
        Rotation2d.fromDegrees(0),
        
        "d3 c4 b6 c8 d9 e10 d11 c10 d9 e8 e6 e4 d3",

        "c1",
        Rotation2d.fromDegrees(180),
        
        config.setKinematics(kinematics).addConstraints(constraints)    
    );
    
    
    public DriveSlalomPath(Drivetrain drivetrain) {
        super(
            new DrivetrainRamseteCommand(drivetrain, trajectory).robotRelative()
        );
    }
}
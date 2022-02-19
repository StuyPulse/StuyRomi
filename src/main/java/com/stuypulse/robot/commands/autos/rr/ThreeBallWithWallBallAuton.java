/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.autos.rr;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.DrivetrainRamseteCommand;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class ThreeBallWithWallBallAuton extends SequentialCommandGroup {
    /** Creates a new ThreeBallWithWallBallAuton. */
    private static final double START_DELAY = 0.5;

    // Time it takes for the intake to go down
    private static final double INTAKE_FALL_DOWN = 0.1;
    // Time it takes for the shooter to reach the target speed
    private static final double SHOOTER_INITIALIZE_DELAY = 1.0;
    // Time it takes for the conveyor to give the shooter the ball
    private static final double CONVEYOR_TO_SHOOTER = 1.0;
    // Time we want to give the drivetrain to align
    private static final double DRIVETRAIN_ALIGN_TIME = 2.0;

    private static final String THREE_BALL_GET_RING_BALL =
            "ThreeBallWithWallBallAuton/output/ThreeBallAutonGetRingBall.wpilib.json";
    private static final String THREE_BALL_GET_WALL_BALL =
            "ThreeBallWithWallBallAuton/output/ThreeBallAutonGetWallBall.wpilib.json";

    public ThreeBallWithWallBallAuton(RobotContainer robot) {
        addCommands(new WaitCommand(START_DELAY));
        addCommands(
                new WaitCommand(INTAKE_FALL_DOWN),
                new WaitCommand(SHOOTER_INITIALIZE_DELAY));

        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, THREE_BALL_GET_RING_BALL),
                new WaitCommand(DRIVETRAIN_ALIGN_TIME),
                new WaitCommand(CONVEYOR_TO_SHOOTER));
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, THREE_BALL_GET_WALL_BALL),
                new WaitCommand(DRIVETRAIN_ALIGN_TIME),
                new WaitCommand(CONVEYOR_TO_SHOOTER));
        }
}

/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.autos.rr;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.DrivetrainRamseteCommand;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class FourBallWithWallBallAuton extends SequentialCommandGroup {
    // Time it takes for the intake to go down
    private static final double INTAKE_FALL_DOWN = 0.1;
    // Time it takes for the shooter to reach the target speed
    private static final double SHOOTER_INITIALIZE_DELAY = 1.0;
    // Time it takes for the conveyor to give the shooter the ball
    private static final double CONVEYOR_TO_SHOOTER = 1.0;
    // Time we want to give the drivetrain to align
    private static final double DRIVETRAIN_ALIGN_TIME = 3.0;

    private static final String FOUR_BALL_GET_WALL_BALL =
            "FourBallWithWallBall/output/FourBallAcquireWallBall.wpilib.json";
    private static final String FOUR_BALL_GET_RING_BALL =
            "FourBallWithWallBall/output/FourBallGetRingBall.wpilib.json";
    private static final String FOUR_BALL_GET_TERMINAL_BALLS =
            "FourBallWithWallBall/output/FourBallGetTerminalBalls.wpilib.json";
    private static final String FOUR_BALL_SHOOT_TERMINAL_BALLS =
            "FourBallWithWallBall/output/FourBallShootTerminalBalls.wpilib.json";

    /** Creates a new FourBallAutonWallBallFirst. */
    public FourBallWithWallBallAuton(RobotContainer robot) {
        // Starting up subsystems
        addCommands(
                new WaitCommand(INTAKE_FALL_DOWN),
                new WaitCommand(SHOOTER_INITIALIZE_DELAY));
        addCommands(
               
                new DrivetrainRamseteCommand(robot.drivetrain, FOUR_BALL_GET_WALL_BALL)
                        .robotRelative(),
                new WaitCommand(DRIVETRAIN_ALIGN_TIME),
                new WaitCommand(CONVEYOR_TO_SHOOTER));
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, FOUR_BALL_GET_RING_BALL)
                        .fieldRelative(),
                new WaitCommand(DRIVETRAIN_ALIGN_TIME),
                new WaitCommand(CONVEYOR_TO_SHOOTER));
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, FOUR_BALL_GET_TERMINAL_BALLS)
                        .fieldRelative());
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, FOUR_BALL_SHOOT_TERMINAL_BALLS)
                        .fieldRelative(),
                new WaitCommand(DRIVETRAIN_ALIGN_TIME),
                new WaitCommand(CONVEYOR_TO_SHOOTER));
    }
}

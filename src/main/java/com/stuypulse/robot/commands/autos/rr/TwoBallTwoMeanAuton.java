/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.autos.rr;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.DrivetrainRamseteCommand;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

/*-
 * @author Vincent Wang (vinowang921@gmail.com)
 * @author Ivan Wei (ivanw8288@gmail.com)
 * @author Ivan Chen (ivanchen07@gmail.com)
 * @author Eric Lin (ericlin071906@gmail.com)
 * @author Marc Jiang (mjiang05@gmail.com)
 * @author Ian Jiang (ijiang05@gmail.com)
 * @author Carmin Vuong (carminvuong@gmail.com)
 * @author Samuel Chen(samchen1738@gmail.com)
 */

public class TwoBallTwoMeanAuton extends SequentialCommandGroup {
    // Initial delay for the auton
    private static final double START_DELAY = 1.0;
    // Time it takes for the shooter to reach the target speed
    private static final double SHOOTER_INITIALIZE_DELAY = 1.0;
    // Time it takes for the conveyor to give the shooter the ball
    private static final double CONVEYOR_TO_SHOOTER = 1.0;
    // Time we want to give the drivetrain to align
    private static final double DRIVETRAIN_ALIGN_TIME = 4.0;

    private static final String TWO_BALL_TO_SECOND_BALL = "TwoBallTwoMeanAuton/output/TwoBallGetSecondBall.wpilib.json";
    private static final String TWO_BALL_GET_OTHER_OPPONENT_BALL = "TwoBallTwoMeanAuton/output/TwoBallGetOtherOpponentBall.wpilib.json";
    private static final String TWO_BALL_GET_WALL_BALL = "TwoBallTwoMeanAuton/output/TwoBallGetWallBall.wpilib.json";
    private static final String TWO_BALL_EJECT_WALL_BALL = "TwoBallTwoMeanAuton/output/TwoBallEjectWallBall.wpilib.json";
    private static final String TWO_BALL_WALL_BALL_INVERSE = "TwoBallTwoMeanAuton/output/TwoBallWallBallInverse.wpilib.json";
    private static final String TWO_BALL_TO_TELEOP_STARTING_POSITION = "TwoBallTwoMeanAuton/output/TwoBallTeleopStart.wpilib.json";


    public TwoBallTwoMeanAuton(RobotContainer robot) {

        // Starting up subsystems
        addCommands(
            new WaitCommand(SHOOTER_INITIALIZE_DELAY));
        // Shoot Two Balls
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, TWO_BALL_TO_SECOND_BALL)
                        .robotRelative(),
                new WaitCommand( DRIVETRAIN_ALIGN_TIME));
        // Get opponent Ball
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, TWO_BALL_GET_OTHER_OPPONENT_BALL)
                        .fieldRelative()
                );
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, TWO_BALL_GET_WALL_BALL)
                        .fieldRelative()
         );
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, TWO_BALL_EJECT_WALL_BALL)
                        .fieldRelative(),
                new WaitCommand(1));

        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, TWO_BALL_WALL_BALL_INVERSE)
                        .fieldRelative());
    }
}
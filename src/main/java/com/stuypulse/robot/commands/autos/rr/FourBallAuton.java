/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.autos.rr;

import com.stuypulse.robot.commands.DrivetrainRamseteCommand;
import com.stuypulse.robot.RobotContainer;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class FourBallAuton extends SequentialCommandGroup {

    private static final String ACQUIRE_FIRST_BALL =
            "FourBallAuton/output/FourBallAutonGetSecondBall.wpilib.json";
    private static final String SECOND_SHOT_PATH =
            "FourBallAuton/output/FourBallAutonGetThirdBall.wpilib.json";
    private static final String SHOOT_THIRD_BALL =
            "FourBallAuton/output/FourBallAutonShootThirdBall.wpilib.json";

    private static final int SHOOTER_START_TIME = 1;
    private static final int SHOOTER_SHOOT_TIME = 1;
    private static final int LIMELIGHT_ALIGN_TIME = 2;
    private static final int HUMAN_PLAYER_WAIT_TIME = 2;

    /** Creates a new ThreeBallAuton. */
    public FourBallAuton(RobotContainer robot) {
        addCommands(
                new WaitCommand(SHOOTER_START_TIME)
        ); // 2 seconds added for intake init time``
        addCommands(
                // new DriveDistanceCommand(robot.drivetrain)
                new DrivetrainRamseteCommand(robot.drivetrain, ACQUIRE_FIRST_BALL).robotRelative(),
                new WaitCommand(LIMELIGHT_ALIGN_TIME),
                new WaitCommand(SHOOTER_SHOOT_TIME)
        );
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, SECOND_SHOT_PATH).fieldRelative(),
                new WaitCommand(HUMAN_PLAYER_WAIT_TIME)
        );
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, SHOOT_THIRD_BALL).fieldRelative(),
                new WaitCommand(LIMELIGHT_ALIGN_TIME),
                new WaitCommand(SHOOTER_SHOOT_TIME)
        );
    }
}

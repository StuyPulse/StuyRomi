/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.autos.rr;

import com.stuypulse.robot.commands.DrivetrainRamseteCommand;
import com.stuypulse.robot.RobotContainer;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class ThreeBallAuton extends SequentialCommandGroup {

    private static final String ACQUIRE_FIRST_BALL =
            "ThreeBallAuton/output/ThreeBallAutonGetSecondBall.wpilib.json";
    private static final String SECOND_SHOT_PATH =
            "ThreeBallAuton/output/ThreeBallAutonGetThirdBall.wpilib.json";
    private static final String SHOOT_THIRD_BALL =
            "ThreeBallAuton/output/ThreeBallAutonShootThirdBall.wpilib.json";

    private static final int SHOOTER_START_TIME = 1;
    private static final int SHOOTER_SHOOT_TIME = 1;
    private static final int LIMELIGHT_ALIGN_TIME = 2;

    /** Creates a new ThreeBallAuton. */
    public ThreeBallAuton(RobotContainer robot) {
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
                new DrivetrainRamseteCommand(robot.drivetrain, SECOND_SHOT_PATH).fieldRelative()
        );
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, SHOOT_THIRD_BALL).fieldRelative(),
                new WaitCommand(LIMELIGHT_ALIGN_TIME),
                new WaitCommand(SHOOTER_SHOOT_TIME)
        );
    }
}

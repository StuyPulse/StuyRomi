/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.autos.rr;

import com.stuypulse.robot.commands.DrivetrainRamseteCommand;
import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.DrivetrainDriveForeverCommand;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class ThreeBallAuton extends SequentialCommandGroup {

    private static final String ACQUIRE_FIRST_BALL =
            "ThreeBallAuton/output/3BallAutonStartPath.wpilib.json";
    private static final String SECOND_SHOT_PATH =
            "ThreeBallAuton/output/ThreeBallAutonGetThirdBall.wpilib.json";

    private static final int SHOOTER_START_TIME = 1;
    private static final int SHOOTER_SHOOT_TIME = 2;
    private static final int LIMELIGHT_ALIGN_TIME = 3;

    /** Creates a new ThreeBallAuton. */
    public ThreeBallAuton(RobotContainer robot) {
        addCommands(
                new WaitCommand(SHOOTER_START_TIME + 2)); // 2 seconds added for intake init time
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, ACQUIRE_FIRST_BALL).robotRelative(),
                new DrivetrainDriveForeverCommand(robot.drivetrain).withTimeout(LIMELIGHT_ALIGN_TIME),
                new WaitCommand(SHOOTER_SHOOT_TIME)
        );
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, SECOND_SHOT_PATH).fieldRelative(),
                new DrivetrainDriveForeverCommand(robot.drivetrain).withTimeout(LIMELIGHT_ALIGN_TIME),
                new WaitCommand(SHOOTER_SHOOT_TIME)
        );
    }
}

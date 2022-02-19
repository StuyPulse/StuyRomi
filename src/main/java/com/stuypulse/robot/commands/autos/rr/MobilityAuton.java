/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.autos.rr;


import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.DriveDistanceCommand;
import com.stuypulse.robot.commands.DrivetrainDriveForeverCommand;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public final class MobilityAuton {
    public static class WithEncoders extends SequentialCommandGroup {
        private static final double START_TO_BALL = 2.0;

        public WithEncoders(RobotContainer robot) {
            addCommands(new DriveDistanceCommand(robot.drivetrain, START_TO_BALL));
        }
    }

    public static class NoEncoders extends SequentialCommandGroup {
        private static final double DRIVE_SPEED = 0.3;
        private static final double DURATION = 5.0;

        public NoEncoders(RobotContainer robot) {
            addCommands(new DrivetrainDriveForeverCommand(robot.drivetrain, DRIVE_SPEED).withTimeout(DURATION));
        }
    }

    private MobilityAuton() {
    }
}
/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.autos.rr;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.RobotContainer;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class MobilityAuton {
    public static class NoEncoders extends SequentialCommandGroup {
        public NoEncoders(RobotContainer robot) {
            addCommands(new DriveDistanceCommand(robot.drivetrain, Constants.Drivetrain.TRACK_WIDTH * 3));
        }
    }
}

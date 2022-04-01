/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.autos.rr;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.DriveDistanceCommand;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

/*-
 * 
 */

public class TwoBallAuton extends SequentialCommandGroup {
    // Time it takes for the shooter to reach the target speed
    private static final double SHOOTER_INITIALIZE_DELAY = 1.2;

    private static final double CONVEYOR_TO_SHOOTER = 1;

    private static final double DRIVETRAIN_ALIGN_TIME = 2;

    private static final double START_TO_BALL = 2.0; // meters

    public TwoBallAuton(RobotContainer robot) {

        // Starting up subsystems
        addCommands(
            new WaitCommand(SHOOTER_INITIALIZE_DELAY) // add amount of time it takes for intake to init
        );

        addCommands(
            new DriveDistanceCommand(robot.drivetrain, START_TO_BALL),
            new WaitCommand(DRIVETRAIN_ALIGN_TIME),
            new WaitCommand(DRIVETRAIN_ALIGN_TIME),
            new WaitCommand(CONVEYOR_TO_SHOOTER)
        );
    }
}

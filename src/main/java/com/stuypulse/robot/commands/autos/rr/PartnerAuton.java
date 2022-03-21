/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.autos.rr;
import com.stuypulse.robot.commands.DrivetrainRamseteCommand;

import com.stuypulse.robot.RobotContainer;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class PartnerAuton extends SequentialCommandGroup {

    
    private static final int SHOOTER_INITIALIZE_DELAY = 1;
    private static final int WAIT_FOR_SECOND_BALL = 2;
    private static final int DRIVETRAIN_ALIGN_TIME = 2;
    private static final int SHOOTER_SHOOT_TIME = 2;

    private static final String ACQUIRE_FIRST_BALL =
            "ThreeBallPartnerAuton/output/ThreeBallAutonGetSecondBall.wpilib.json";


    /** Creates a new ThreeBallAuton. */
    public PartnerAuton(RobotContainer robot) {

        // Starting up subsystems
        addCommands(
            new WaitCommand(SHOOTER_INITIALIZE_DELAY)
        );

        addCommands(
            new WaitCommand(SHOOTER_SHOOT_TIME),
            new WaitCommand(WAIT_FOR_SECOND_BALL), // .withInterrupt(robot.conveyor::hasBothBalls)
            new WaitCommand(SHOOTER_SHOOT_TIME) 
        );

        addCommands(
            new DrivetrainRamseteCommand(robot.drivetrain, ACQUIRE_FIRST_BALL).robotRelative()
        );

        addCommands(
            new WaitCommand(DRIVETRAIN_ALIGN_TIME),
            new WaitCommand(SHOOTER_SHOOT_TIME) 
        );
        
    }
}

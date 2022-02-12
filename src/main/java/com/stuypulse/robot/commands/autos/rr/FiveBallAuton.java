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
 * @author many other software newbies who left before writing author tags
 */

public class FiveBallAuton extends SequentialCommandGroup {

        private static final int SHOOTER_INITIALIZE_DELAY = 1;
        // Time it takes for the conveyor to give the shooter the ball
        private static final int CONVEYOR_TO_SHOOTER = 1;
        // Time we want to give the drivetrain to align
        private static final int DRIVETRAIN_ALIGN_TIME = 2;
    
        private static final int HUMAN_WAIT_TIME = 2;

    private static final String FIVE_BALL_GET_FIRST_TWO_BALLS =
            "FiveBallAuton/output/FiveBallAcquireSecondBall.wpilib.json";
    private static final String FIVE_BALL_GET_TERMINAL_BALLS =
            "FiveBallAuton/output/FiveBallGetTerminalBalls.wpilib.json";
    private static final String FIVE_BALL_SHOOT_TERMINAL_BALLS =
            "FiveBallAuton/output/FiveBallShootTerminalBalls.wpilib.json";
    private static final String FIVE_BALL_SHOOT_WALL_BALL =
            "FiveBallAuton/output/FiveBallShootWallBall.wpilib.json";

    /** Creates a new FiveBallAuton. */
    public FiveBallAuton(RobotContainer robot) {

        // Starting up subsystems
        addCommands(
                new WaitCommand(SHOOTER_INITIALIZE_DELAY)
        );

        // Tarmac to first ball
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, FIVE_BALL_GET_FIRST_TWO_BALLS).robotRelative()
        );

        // First ball to terminal to RingShot
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, FIVE_BALL_GET_TERMINAL_BALLS).fieldRelative(),
                new WaitCommand(HUMAN_WAIT_TIME)
        );

        // Return to Ring to shoot
        //
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, FIVE_BALL_SHOOT_TERMINAL_BALLS).fieldRelative(),
                new WaitCommand(DRIVETRAIN_ALIGN_TIME),
                new WaitCommand(CONVEYOR_TO_SHOOTER)
        );

        // Pick up and shoot fifth ball
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, FIVE_BALL_SHOOT_WALL_BALL).fieldRelative(),
                new WaitCommand(DRIVETRAIN_ALIGN_TIME),
                new WaitCommand(CONVEYOR_TO_SHOOTER)
        );
    }
}

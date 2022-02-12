/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.autos;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.DrivetrainDriveForeverCommand;
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
    
    // we need to set these after testing
    private static final int FIVE_BALL_START_PATH_TIMEOUT = 5;
    private static final int FIVE_BALL_TERMINAL_TO_SHOOT_TIMEOUT = 4;
    private static final int FIVE_BALL_TO_TERIMAL_TIMEOUT = 3;
    private static final int FIVE_BALL_GET_LAST_BALL_TIMEOUT = 2;

    private static final String FIVE_BALL_START_PATH =
            "FiveBallAuton/output/FiveBallStartPath.wpilib.json";
    private static final String FIVE_BALL_TO_TERMINAL =
            "FiveBallAuton/output/FiveBallTerminalPath.wpilib.json";
    private static final String FIVE_BALL_TERMINAL_TO_SHOOT =
            "FiveBallAuton/output/FiveBallTerminalToShoot.wpilib.json";
    private static final String FIVE_BALL_GET_LAST_BALL =
            "FiveBallAuton/output/FiveBallGetLastBallPath.wpilib.json";

    /** Creates a new FiveBallAuton. */
    public FiveBallAuton(RobotContainer robot) {

        // Starting up subsystems
        addCommands(
                new WaitCommand(SHOOTER_INITIALIZE_DELAY + 1) // add 1 second for intake extend and intake acquire forever
        );

        // Tarmac to first ball
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, FIVE_BALL_START_PATH)
                        .withTimeout(FIVE_BALL_START_PATH_TIMEOUT)
        );

        // First ball to terminal to RingShot
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, FIVE_BALL_TO_TERMINAL)
                        .withTimeout(FIVE_BALL_TO_TERIMAL_TIMEOUT),
                new WaitCommand(HUMAN_WAIT_TIME),
                new DrivetrainDriveForeverCommand(robot.drivetrain).withTimeout(DRIVETRAIN_ALIGN_TIME),
                new WaitCommand(CONVEYOR_TO_SHOOTER)
        );

        // Return to Ring to shoot
        //
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, FIVE_BALL_TERMINAL_TO_SHOOT)
                        .withTimeout(FIVE_BALL_TERMINAL_TO_SHOOT_TIMEOUT),
                new DrivetrainDriveForeverCommand(robot.drivetrain).withTimeout(2)
        );

        // Pick up and shoot fifth ball
        addCommands(
                new DrivetrainRamseteCommand(robot.drivetrain, FIVE_BALL_GET_LAST_BALL)
                        .withTimeout(FIVE_BALL_GET_LAST_BALL_TIMEOUT),
                new DrivetrainDriveForeverCommand(robot.drivetrain).withTimeout(DRIVETRAIN_ALIGN_TIME),
                new WaitCommand(CONVEYOR_TO_SHOOTER)
        );
    }
}

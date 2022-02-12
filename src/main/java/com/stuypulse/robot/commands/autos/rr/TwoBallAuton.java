/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.autos.rr;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.DrivetrainDriveForeverCommand;

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

public class TwoBallAuton extends SequentialCommandGroup {
    // Time it takes for the shooter to reach the target speed
    private static final int SHOOTER_INITIALIZE_DELAY = 1;

    private static final int CONVEYOR_TO_SHOOTER = 1;

    private static final int DRIVETRAIN_ALIGN_TIME = 2;

    public TwoBallAuton(RobotContainer robot) {
        // Starting up subsystems
        addCommands(
            new WaitCommand(SHOOTER_INITIALIZE_DELAY + 1) // add amount of time it takes for intake to init
        );

        addCommands(
            new DriveDistanceCommand(robot.drivetrain, Constants.Drivetrain.TRACK_WIDTH * 4),
            new DrivetrainDriveForeverCommand(robot.drivetrain).withTimeout(DRIVETRAIN_ALIGN_TIME),
            new WaitCommand(DRIVETRAIN_ALIGN_TIME),
            new WaitCommand(CONVEYOR_TO_SHOOTER)
        );
    }
}

package com.stuypulse.robot.commands.autos.rr;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.DriveDistanceCommand;

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

public class OneBallAuton extends SequentialCommandGroup {
    // Time it takes for the shooter to reach the target speed
    private static final double SHOOTER_INITIALIZE_DELAY = 1.0;

    private static final double CONVEYOR_TO_SHOOTER = 0.25;

    private static final double DRIVETRAIN_ALIGN_TIME = 2;

    private static final double START_TO_BALL = 2.0; // meters


    public OneBallAuton(RobotContainer robot) {
        // Starting up subsystems
        addCommands(
                new WaitCommand(SHOOTER_INITIALIZE_DELAY)
        );
        addCommands(
                new DriveDistanceCommand(robot.drivetrain, START_TO_BALL),
                new WaitCommand(DRIVETRAIN_ALIGN_TIME),
                new WaitCommand(CONVEYOR_TO_SHOOTER)
        );
    }
}
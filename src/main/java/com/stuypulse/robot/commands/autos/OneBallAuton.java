package com.stuypulse.robot.commands.autos;

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

public class OneBallAuton extends SequentialCommandGroup {
    // Time it takes for the shooter to reach the target speed
    private static final int SHOOTER_INITIALIZE_DELAY = 1;

    private static final int DRIVETRAIN_ALIGN_TIME = 2;

    private static final int CONVEYOR_TO_SHOOTER = 2;

    public OneBallAuton(RobotContainer robot) {
        // Starting up subsystems
        addCommands(
                new WaitCommand(SHOOTER_INITIALIZE_DELAY + 1) // Add 1 second for starting up intake
        );
        addCommands(
                new DrivetrainDriveForeverCommand(robot.drivetrain).withTimeout(2), // 2 seconds for testing
                new DrivetrainDriveForeverCommand(robot.drivetrain).withTimeout(DRIVETRAIN_ALIGN_TIME),
                new WaitCommand(CONVEYOR_TO_SHOOTER)
        );
    }
}
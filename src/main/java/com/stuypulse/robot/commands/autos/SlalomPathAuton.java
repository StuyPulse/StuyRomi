package com.stuypulse.robot.commands.autos;

import com.stuypulse.robot.commands.DrivetrainRamseteCommand;
import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class SlalomPathAuton extends SequentialCommandGroup {
    
    // TODO: Add Working Path
    private static final String FILE_NAME = "SlalomPath/...";

    public SlalomPathAuton(Drivetrain drivetrain) {
        super(
            new DrivetrainRamseteCommand(drivetrain, FILE_NAME).robotRelative()
        );
    }

}

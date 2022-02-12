package com.stuypulse.robot.commands.autos.irh;

import com.stuypulse.robot.commands.DrivetrainRamseteCommand;
import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class BouncePathAuton extends SequentialCommandGroup {
    
    public BouncePathAuton(Drivetrain drivetrain) {
        super(
            new DrivetrainRamseteCommand(drivetrain, "BouncePath/Part1.wpilib.json").robotRelative(),
            new DrivetrainRamseteCommand(drivetrain, "BouncePath/Part2.wpilib.json").fieldRelative(),
            new DrivetrainRamseteCommand(drivetrain, "BouncePath/Part3.wpilib.json").fieldRelative(),
            new DrivetrainRamseteCommand(drivetrain, "BouncePath/Part4.wpilib.json").fieldRelative()
        );
    }

}

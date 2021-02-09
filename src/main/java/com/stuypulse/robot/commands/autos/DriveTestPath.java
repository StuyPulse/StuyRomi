/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.stuypulse.robot.commands.autos;

import com.stuypulse.robot.commands.DrivetrainRamseteCommand;
import com.stuypulse.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;


// NOTE:  Consider using this command inline, rather than writing a subclass.  
// For more information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html

public class DriveTestPath extends SequentialCommandGroup {

    public DriveTestPath(Drivetrain drivetrain) {
        super(
            new DrivetrainRamseteCommand(drivetrain, "TestPath/output/SnakeE1-A10.wpilib.json").robotRelative(),    
            new DrivetrainRamseteCommand(drivetrain, "TestPath/output/Figure8BackHome.wpilib.json").fieldRelative()
        );
    }
}
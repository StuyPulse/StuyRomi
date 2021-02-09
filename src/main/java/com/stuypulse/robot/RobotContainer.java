// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.stuypulse.robot;

import com.stuypulse.robot.commands.DrivetrainDriveCommand;
import com.stuypulse.robot.commands.DrivetrainRamseteCommand;
import com.stuypulse.robot.commands.autos.DriveTestPath;
import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.subsystems.OnBoardIO;
import com.stuypulse.robot.subsystems.OnBoardIO.ChannelMode;
import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.input.gamepads.Xbox;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystems and commands are defined here...
    private final Drivetrain drivetrain = new Drivetrain();
    private final OnBoardIO onBoardIO = new OnBoardIO(ChannelMode.INPUT, ChannelMode.INPUT);

    // Assumes a gamepad plugged into channnel 0
    private final Gamepad driver = new Xbox(0);

    public RobotContainer() {
        configureDefaultCommands();
        configureButtonBindings();
    }

    private void configureDefaultCommands() {
        drivetrain.setDefaultCommand(new DrivetrainDriveCommand(drivetrain, driver));
        onBoardIO.getButtonA().whenPressed(getAutonomousCommand());
    }

    private void configureButtonBindings() {
        driver.getBottomButton().whenPressed(() -> drivetrain.reset());
    }

    // Autonomous Commands
    public Command getAutonomousCommand() {
        //return new DrivetrainRamseteCommand(drivetrain, "output/Bounce Path 1.wpilib.json");
        //return new DrivetrainRamseteCommand(drivetrain, "output/Bounce Path 2.wpilib.json");
        //return new DrivetrainRamseteCommand(drivetrain, "output/Racing Barrel 1.wpilib.json");
        return new DrivetrainRamseteCommand(drivetrain, "output/SlalomPath.wpilib.json");
        //return new DriveBouncePath1(drivetrain);
        //return new DriveBouncePath2(drivetrain);
        //return new DriveSlalomPath(drivetrain);
        //return new DriveTestPath(drivetrain);
    }

}

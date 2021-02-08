// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.stuypulse.robot;

import edu.wpi.first.wpilibj2.command.Command;

import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.subsystems.OnBoardIO;
import com.stuypulse.robot.subsystems.OnBoardIO.ChannelMode;

import java.io.IOException;

import com.stuypulse.robot.commands.*;
import com.stuypulse.robot.commands.autos.*;

import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.input.gamepads.*;

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
    public Command getAutonomousCommand(String file) {
        try {
            return new DrivetrainRamseteCommand(drivetrain, file);
        } catch (IOException e) {
            System.err.println("Error Opening \"" + file + "\", Reverting to DoNothingAuton()!");
            System.out.println(e.getStackTrace());
            return new DoNothingAuton();
        }
    }

    public Command getAutonomousCommand() {
        return getAutonomousCommand("output/Racing Barrel 1.wpilib.json");

        // return new DriveBouncePath1(drivetrain);
    }

}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.stuypulse.robot;

import com.stuypulse.robot.commands.*;
import com.stuypulse.robot.subsystems.Drivetrain;
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

    // Assumes a gamepad plugged into channnel 0
    private final Gamepad driver = new Xbox(0);

    // The robot's subsystems and commands are defined here...
    private final Drivetrain drivetrain = new Drivetrain();

    public RobotContainer() {
        configureDefaultCommands();
        configureButtonBindings();
    }

    private void configureDefaultCommands() {
        drivetrain.setDefaultCommand(new DrivetrainDriveCommand(drivetrain, driver));
    }

    private void configureButtonBindings() {
        driver.getLeftButton().whenPressed(new DrivetrainResetCommand(drivetrain));
        driver.getRightButton().whileHeld(new DrivetrainSpinCommand(drivetrain));
    }

    // Autonomous Commands
    public Command getAutonomousCommand() {
        return null;
    }

}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.stuypulse.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.subsystems.OnBoardIO;
import com.stuypulse.robot.subsystems.OnBoardIO.ChannelMode;

import java.io.IOException;

import com.stuypulse.robot.commands.*;
import com.stuypulse.robot.commands.autos.*;

import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.input.gamepads.*;
import com.stuypulse.stuylib.control.*;
import com.stuypulse.stuylib.math.*;

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
        configureAutons();
    }

    private void configureDefaultCommands() {
        drivetrain.setDefaultCommand(new DrivetrainDriveCommand(drivetrain, driver));
    }

    private void configureButtonBindings() {
        driver.getBottomButton().whenPressed(() -> drivetrain.reset());
    }

    private static final SendableChooser<Command> AUTONS = new SendableChooser<>();

    private void configureAutons() {
        AUTONS.setDefaultOption("No Auton", new DoNothingAuton());

        // List autons here:
        AUTONS.addOption("Drive-S", new DriveSCommand(drivetrain));

        SmartDashboard.putData("Autonomous Commands", AUTONS);
    }

    // Use SmartDashboard to select auton routine
    public Command getAutonomousCommand() throws IOException {
        return new DrivetrainRamseteCommand(drivetrain, "output/Test.wpilib.json");
    }

}

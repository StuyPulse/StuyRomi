// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.stuypulse.robot;

import com.stuypulse.robot.commands.DrivetrainDriveCommand;
import com.stuypulse.robot.commands.DrivetrainResetCommand;
import com.stuypulse.robot.commands.DrivetrainSpinCommand;
import com.stuypulse.robot.commands.autos.irh.BouncePathAuton;
import com.stuypulse.robot.commands.autos.rr.DriveDistanceCommand;
import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.subsystems.OnBoardIO;
import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.input.gamepads.keyboard.SimKeyGamepad;

import edu.wpi.first.wpilibj.livewindow.LiveWindow;
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
    private final Gamepad driver = new SimKeyGamepad();

    // The robot's subsystems and commands are defined here...
    private final Drivetrain drivetrain = new Drivetrain();
    private final OnBoardIO onBoardIO = new OnBoardIO(OnBoardIO.ChannelMode.INPUT, OnBoardIO.ChannelMode.INPUT);

    public RobotContainer() {
        LiveWindow.disableAllTelemetry();

        configureDefaultCommands();
        configureButtonBindings();
    }

    private void configureDefaultCommands() {
        drivetrain.setDefaultCommand(new DrivetrainDriveCommand(drivetrain, driver));
    }

    private void configureButtonBindings() {
        driver.getBottomButton().whileHeld(new DrivetrainSpinCommand(drivetrain));
        driver.getTopButton().whenPressed(new DrivetrainResetCommand(drivetrain));

        driver.getLeftButton().whenPressed(new DriveDistanceCommand(drivetrain, +1));
        driver.getRightButton().whenPressed(new DriveDistanceCommand(drivetrain, -1));

        onBoardIO.getButtonA().whenPressed(getAutonomousCommand());
    }

    // Autonomous Commands
    public Command getAutonomousCommand() {
        
        // return new BarrelRacingAuton(drivetrain);

        return new BouncePathAuton(drivetrain);
        
        // return new SlalomPathAuton(drivetrain);

        // return new DoNothingAuton();
    }

}

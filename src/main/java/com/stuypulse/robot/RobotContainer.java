// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.stuypulse.robot;

import com.stuypulse.robot.commands.DriveDistanceCommand;
import com.stuypulse.robot.commands.DrivetrainDriveCommand;
import com.stuypulse.robot.commands.DrivetrainResetCommand;
import com.stuypulse.robot.commands.DrivetrainSpinCommand;
import com.stuypulse.robot.commands.autos.rr.OneBallAuton;
import com.stuypulse.robot.commands.autos.rr.TwoBallAuton;
import com.stuypulse.robot.commands.autos.rr.TwoBallTwoMeanAuton;
import com.stuypulse.robot.commands.autos.rr.ThreeBallAuton;
import com.stuypulse.robot.commands.autos.rr.FourBallAuton;
import com.stuypulse.robot.commands.autos.rr.MobilityAuton;
import com.stuypulse.robot.commands.autos.rr.FiveBallAuton;
import com.stuypulse.robot.commands.autos.rr.FiveBallBlayAuton;
import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.robot.subsystems.OnBoardIO;
import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.input.gamepads.keyboard.SimKeyGamepad;

import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    public final Drivetrain drivetrain = new Drivetrain();
    private final OnBoardIO onBoardIO = new OnBoardIO(OnBoardIO.ChannelMode.INPUT, OnBoardIO.ChannelMode.INPUT);

    private static SendableChooser<Command> autoChooser = new SendableChooser<>();

    public RobotContainer() {
        LiveWindow.disableAllTelemetry();

        configureAutons();
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
    }

    private void configureAutons() {
        autoChooser.setDefaultOption("No Encoders (Moby Dick)", new MobilityAuton.NoEncoders(this));
        autoChooser.addOption("With Encoders (Moby Dick)", new MobilityAuton.WithEncoders(this));
        autoChooser.addOption("One Ball", new OneBallAuton(this));
        autoChooser.addOption("Two Ball", new TwoBallAuton(this));
        autoChooser.addOption("Three Ball", new ThreeBallAuton(this));
        autoChooser.addOption("Four Ball", new FourBallAuton(this));
        autoChooser.addOption("Five Ball", new FiveBallAuton(this));
        autoChooser.addOption("FIve Ball Blay Auton", new FiveBallBlayAuton(this));
        autoChooser.addOption("Two Ball Mean Auton", new TwoBallTwoMeanAuton(this));

        SmartDashboard.putData("Auto", autoChooser);
    }

    // Autonomous Commands]
    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }

}

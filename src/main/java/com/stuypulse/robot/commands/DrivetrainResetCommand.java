package com.stuypulse.robot.commands;

import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class DrivetrainResetCommand extends InstantCommand {
    
    private final Drivetrain drivetrain;
    
    public DrivetrainResetCommand (Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }
    
    
    @Override
    public void initialize() {
        drivetrain.stop();
    }

}

package com.stuypulse.robot.commands;

import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DrivetrainSpinCommand extends CommandBase {

    private final Drivetrain drivetrain;

    public DrivetrainSpinCommand(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.reset();
    }

    @Override
    public void execute() {
        drivetrain.arcadeDrive(0.0, 1.0);
    }

    @Override
    public void end(boolean isInterrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

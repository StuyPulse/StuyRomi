package com.stuypulse.robot.commands;

import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DrivetrainSpinCommand extends CommandBase {
    
    Drivetrain drivetrain;

    public DrivetrainSpinCommand(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.stop();
        drivetrain.reset();
    }

    @Override
    public void execute() {
        // Spins at max speed
        drivetrain.arcadeDrive(0.0, 1.0);
    }

    @Override
    public void end(boolean isInterrupted) {
        drivetrain.stop();
    }

    @Override
    public boolean isFinished() {
        // Its impossible to spin too much
        return false;
    }

}

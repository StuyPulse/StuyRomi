package com.stuypulse.robot.commands;

import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.stuylib.input.Gamepad;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DrivetrainDriveCommand extends CommandBase {
    
    private final Drivetrain drivetrain; 
    private final Gamepad gamepad;
    
    public DrivetrainDriveCommand(Drivetrain drivetrain, Gamepad gamepad) {
        this.drivetrain = drivetrain;
        this.gamepad = gamepad;
        
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.stop();
    }
     
    @Override
    public void execute() {
        drivetrain.arcadeDrive(gamepad.getRightTrigger() - gamepad.getLeftTrigger(), gamepad.getLeftStick().x);

    }
    
    @Override
    public void end(boolean isInterrupted) {
        drivetrain.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

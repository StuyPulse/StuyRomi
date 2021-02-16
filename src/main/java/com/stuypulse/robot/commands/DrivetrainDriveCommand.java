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
        drivetrain.reset();
        drivetrain.stop();
    }
     
    @Override
    public void execute() {
        // gamepad.getRightStick().x;
        // gamepad.getRightStick().y;
        // gamepad.getLeftStick().x;
        // gamepad.getLeftStick().y;
        // gamepad.getRightTrigger();
        // gamepad.getLeftTrigger();
        //what does the x and y at the end do

        
        drivetrain.arcadeDrive(gamepad.getRightTrigger() - gamepad.getLeftTrigger(), gamepad.getLeftStick().x);

    }
    
    @Override
    public void end(boolean isInterrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

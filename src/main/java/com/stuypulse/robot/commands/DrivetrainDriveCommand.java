package com.stuypulse.robot.commands;

import com.stuypulse.stuylib.input.Gamepad;

import java.security.DrbgParameters.NextBytes;

import com.stuypulse.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

// TODO: Find out what we can use from here
//      - IFilter: https://stuypulse.github.io/StuyLib/com/stuypulse/stuylib/streams/filters/IFilter.html
//      - All of the filters: https://stuypulse.github.io/StuyLib/com/stuypulse/stuylib/streams/filters/package-summary.html
import com.stuypulse.stuylib.streams.filters.*;

public class DrivetrainDriveCommand extends CommandBase {
    
    private final Drivetrain drivetrain; 
    private final Gamepad gamepad;
    private IFilter filterspeed = new IFilterGroup(
        //Lowpass filter: 
        new LowPassFilter(0.3),

        //Moving average filter:
        new MovingAverage(30), 
        
        //RateLimit filter:
        new RateLimit(10)

    );
    private IFilter filterturn = new IFilterGroup(
        //Lowpass filter: 
        new LowPassFilter(0.3),

        //Moving average filter:
        new MovingAverage(30), 
        
        //RateLimit filter:
        new RateLimit(10)
        
    );
    

    public DrivetrainDriveCommand(Drivetrain drivetrain, Gamepad gamepad) {
        this.drivetrain = drivetrain;
        this.gamepad = gamepad;
        
        addRequirements(drivetrain);
        
    }
     
    @Override
    public void execute() {

        double speed = gamepad.getRightTrigger() - gamepad.getLeftTrigger();
        double turn = gamepad.getLeftStick().x;

        // TODO: Filter these values sending them to the drivetrain
        
        
        drivetrain.arcadeDrive(filterspeed.get(speed), filterturn.get(turn));

        
    }
}

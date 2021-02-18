package com.stuypulse.robot.commands;

import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

// TODO: Find out what we can use from here
//      - IFilter: https://stuypulse.github.io/StuyLib/com/stuypulse/stuylib/streams/filters/IFilter.html
//      - All of the filters: https://stuypulse.github.io/StuyLib/com/stuypulse/stuylib/streams/filters/package-summary.html
import com.stuypulse.stuylib.streams.filters.*;

public class DrivetrainDriveCommand extends CommandBase {
    
    private final Drivetrain drivetrain; 
    private final Gamepad gamepad;
    // private IFilter filter = new LowPassFilter(0.5);
    
    private IFilter speedFilter = new IFilterGroup (
        new LowPassFilter (0.2),
        new TimedMovingAverage(3)
    );

    private IFilter turnFilter = new IFilterGroup (
        new LowPassFilter(0.4),
        new TimedMovingAverage(5)
    );
    
    public DrivetrainDriveCommand(Drivetrain drivetrain, Gamepad gamepad) {
        this.drivetrain = drivetrain;
        this.gamepad = gamepad;
        
        addRequirements(drivetrain);
    }
     
    @Override
    public void execute() {

        double speed = speedFilter.get(gamepad.getRightTrigger() - gamepad.getLeftTrigger());
        double turn = turnFilter.get(gamepad.getLeftStick().x);

        // TODO: Filter these values sending them to the drivetrain

        drivetrain.arcadeDrive(speed, turn);

    }
}

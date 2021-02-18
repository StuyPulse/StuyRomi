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
    
    public DrivetrainDriveCommand(Drivetrain drivetrain, Gamepad gamepad) {
        this.drivetrain = drivetrain;
        this.gamepad = gamepad;
        
        addRequirements(drivetrain);
    }
    private IFilter filter = new IFilterGroup(
new LowPassFilter(0.4),

new MovingAverage(10),

new SpeedProfile(3),

new MedianFilter(7)
    );
    private IFilter filter2 = new TimedMovingAverage(0.2);
    @Override
    public void execute() {

        double speed = gamepad.getRightTrigger() - gamepad.getLeftTrigger();
        speed = filter.get(speed);
        double turn = gamepad.getLeftStick().x;
        turn = filter2.get(turn);

        // TODO: Filter these values sending them to the drivetrain

        drivetrain.arcadeDrive(speed, turn);

    }
}

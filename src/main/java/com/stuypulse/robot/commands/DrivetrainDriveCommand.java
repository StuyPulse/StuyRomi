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

    private final IFilter filter = new IFilterGroup (
        (x) -> SLMath.square(x),
        new LowPassFilter(0.7),
        new MovingAverage(10)
    );

    public DrivetrainDriveCommand(Drivetrain drivetrain, Gamepad gamepad) {
        this.drivetrain = drivetrain;
        this.gamepad = gamepad;
        
        addRequirements(drivetrain);
    }

    @Override
    public void initalize() {
        filter = new IFilterGroup (
            (x) -> SLMath.square(x),
            new LowPassFilter(0.7),
            new MovingAverage(10)
        );
    }

    @Override
    public void execute() {

        double speed = gamepad.getRightTrigger() - gamepad.getLeftTrigger();
        double turn = gamepad.getLeftStick().x;

        // TODO: Filter these values sending them to the drivetrain

        drivetrain.arcadeDrive(filter.get(speed), filter.get(turn));
    }
}

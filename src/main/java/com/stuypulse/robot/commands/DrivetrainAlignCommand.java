package com.stuypulse.robot.commands;

import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.stuylib.control.PIDController;
import com.stuypulse.stuylib.network.limelight.Limelight;
import com.stuypulse.stuylib.streams.filters.LowPassFilter;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DrivetrainAlignCommand extends CommandBase {

    private Drivetrain drivetrain;
    private PIDController distanceCtrl;
    private PIDController angleCtrl;

    // larger = closer
    private final int DISTANCE_YANGLE_OFFSET = 10;

    public DrivetrainAlignCommand(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;

        addRequirements(drivetrain);
        distanceCtrl = new PIDController(0.05, 0, 0.00);
        // distanceCtrl.setOutputFilter(new LowPassFilter(0.25));

        angleCtrl = new PIDController(0.01, 0, 0.00);
        // angleCtrl.setOutputFilter(new LowPassFilter(0.25));
    }

    @Override
    public void execute() {

        double xAngle = Limelight.getTargetXAngle();
        double yAngle = Limelight.getTargetYAngle() + DISTANCE_YANGLE_OFFSET;

        drivetrain.arcadeDrive(distanceCtrl.update(yAngle), angleCtrl.update(xAngle));
    }
}

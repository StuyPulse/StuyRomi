package com.stuypulse.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Transform2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import com.stuypulse.robot.subsystems.Drivetrain;

import com.stuypulse.robot.Constants.Drivetrain.Motion;

import java.io.IOException;

import com.stuypulse.robot.Constants;

/**
 * Utility command that follows a trajectory using 
 * a ramsete controller. Useful for motion profiling.
 */
public final class DrivetrainRamseteCommand extends RamseteCommand {
    
    private Drivetrain drivetrain;

    public DrivetrainRamseteCommand(Drivetrain drivetrain, Trajectory trajectory) {
        super(
            // Transform the Trajectory so that it starts at (0, 0)
            trajectory.transformBy(new Transform2d(trajectory.getInitialPose(), new Pose2d())),
            drivetrain::getPose,
            new RamseteController(),
            Motion.MOTOR_FEED_FORWARD,
            Motion.KINEMATICS,
            drivetrain::getWheelSpeeds,
            new PIDController(Motion.PID.P, Motion.PID.I, Motion.PID.D),
            new PIDController(Motion.PID.P, Motion.PID.I, Motion.PID.D),
            drivetrain::tankDriveVolts,
            drivetrain
        );

        this.drivetrain = drivetrain;
    }

    public DrivetrainRamseteCommand(Drivetrain drivetrain, String path) throws IOException {
        this(
            drivetrain,
            
            TrajectoryUtil.fromPathweaverJson(
                Constants.DEPLOY_DIRECTORY.resolve(path)
            )

        );
    }

    @Override
    public void initialize() {
        super.initialize();

        // reset the drivetrain so it begins driveing at (0, 0)
        drivetrain.reset();
    }

}

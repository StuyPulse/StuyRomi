package com.stuypulse.robot.commands;

import java.io.IOException;
import java.util.List;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.Constants.Drivetrain.Motion;
import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.RamseteCommand;

/**
 * Utility command that follows a trajectory using 
 * a ramsete controller. Useful for motion profiling.
 */
public final class DrivetrainRamseteCommand extends RamseteCommand {
    
    private static final Trajectory DEFAULT_TRAJECTORY = TrajectoryGenerator.generateTrajectory(
        new Pose2d(), List.of(), new Pose2d(), new TrajectoryConfig(0, 0).setKinematics(Constants.Drivetrain.Motion.KINEMATICS));

    // Function that gets a trajectory from path weaver, 
    // but will give a default one if it has an issue
    private static Trajectory getTrajectory(String path) {
        Trajectory trajectory = DEFAULT_TRAJECTORY;
        try {
            trajectory = TrajectoryUtil.fromPathweaverJson(
                Constants.DEPLOY_DIRECTORY.resolve(path)
            );
        } catch(IOException e) {
            System.err.println("Error Opening \"" + path + "\"!");
            System.out.println(e.getStackTrace());
        }

        return trajectory;
    }

    private boolean resetPosition;
    private Trajectory trajectory;
    private Drivetrain drivetrain;

    public DrivetrainRamseteCommand(Drivetrain drivetrain, Trajectory trajectory) {
        super(
            trajectory,
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

        this.resetPosition = true;
        this.trajectory = trajectory;
        this.drivetrain = drivetrain;
    }

    public DrivetrainRamseteCommand(Drivetrain drivetrain, String path) {
        this( drivetrain, getTrajectory(path) );
    }

    // [DEFAULT] Resets the 
    public DrivetrainRamseteCommand robotRelative() {
        this.resetPosition = true;
        return this;
    }

    // Make the trajectory relative to the field 
    public DrivetrainRamseteCommand fieldRelative() {
        this.resetPosition = false;
        return this;
    }

    @Override
    public void initialize() {
        super.initialize();

        if(resetPosition)
            drivetrain.reset(trajectory.getInitialPose());
    }

}

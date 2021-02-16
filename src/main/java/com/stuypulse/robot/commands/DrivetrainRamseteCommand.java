package com.stuypulse.robot.commands;

import java.io.IOException;
import java.util.List;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.Constants.Drivetrain.Motion;
import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.RamseteCommand;

/**
 * Utility command that follows a trajectory using 
 * a ramsete controller. Useful for motion profiling.
 */
public final class DrivetrainRamseteCommand extends RamseteCommand {
    
    // Load Trajectory From File
    private static Trajectory getTrajectory(String path) {
        try {
            return TrajectoryUtil.fromPathweaverJson(
                Constants.DEPLOY_DIRECTORY.resolve(path)
            );
        } catch(IOException e) {
            System.err.println("Error Opening \"" + path + "\"!");
            System.out.println(e.getStackTrace());
            return null;
        }
    }

    // Information stored by this command
    private boolean resetPosition;
    private Trajectory trajectory;
    private Drivetrain drivetrain;

    // Setup the Ramsete Command
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

    // Load from file
    public DrivetrainRamseteCommand(Drivetrain drivetrain, String path) {
        this( drivetrain, getTrajectory(path) );
    }

    // Reset encoders if required
    @Override
    public void initialize() {
        super.initialize();

        if(resetPosition) {
            drivetrain.reset(trajectory.getInitialPose());
        }
    }

    // [DEFAULT] Resets the drivetrain to the begining of the trajectory
    public DrivetrainRamseteCommand robotRelative() {
        this.resetPosition = true;
        return this;
    }

    // Make the trajectory relative to the field 
    public DrivetrainRamseteCommand fieldRelative() {
        this.resetPosition = false;
        return this;
    }
}

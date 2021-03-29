package com.stuypulse.robot.commands;

import java.util.List;

import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;

public class DrivetrainGotoCommand extends FollowerCommand {

    private final Pose2d target;

    // this is really messy, i might as well not have a Pose2d target
    // and just always take in 3 target values
    private final Number targetX, targetY, targetDeg;

    private final TrajectoryConfig config;

    // Base constructor - not good to use
    private DrivetrainGotoCommand(
        Drivetrain drivetrain, 
        Pose2d target, 
        Number targetX, Number targetY, Number targetDeg, 
        TrajectoryConfig config
    ) {
        // Don't give the follower command a trajectory (until later)
        super(drivetrain, null);

        // Some of these will be used to create a trajectory
        this.target = target;
        
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetDeg = targetDeg;

        this.config = config;
    }

    public DrivetrainGotoCommand(Drivetrain drivetrain, Pose2d target, TrajectoryConfig config) {
        this(drivetrain, target, null, null, null, config);
    }

    public DrivetrainGotoCommand(
        Drivetrain drivetrain, 
        Number targetX, Number targetY, Number targetDeg,
        TrajectoryConfig config
    ) {
        this(drivetrain, null, targetX, targetY, targetDeg, config);
    }

    // Gets a pose2d to go to 
    private Pose2d getTarget() {
        if (target != null) {
            return target;
        }

        return new Pose2d(
            targetX.doubleValue(),
            targetY.doubleValue(),
            Rotation2d.fromDegrees(targetDeg.doubleValue())
        );
    }

    // Gets a trajectory that starts at the drivetrain's current position
    private Trajectory getTrajectory() {
        // Create the trajectory in the beginning
        Pose2d where = drivetrain.getPose();
        return TrajectoryGenerator.generateTrajectory(
            List.of(
                where,
                getTarget()
            ),
            config
        );
    }

    @Override
    public void initialize() {
        // Update the trajectory, then initialize the follower command
        this.trajectory = getTrajectory();
        super.initialize();
    }

}

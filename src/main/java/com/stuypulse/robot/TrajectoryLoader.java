package com.stuypulse.robot;

import java.io.IOException;
import java.util.List;

import com.stuypulse.robot.Constants.Drivetrain;
import com.stuypulse.robot.Constants.Drivetrain.Motion;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.DriverStation;

public final class TrajectoryLoader {

    private static final TrajectoryConfig MAX_SPEED_TRAJECTORY =
            new TrajectoryConfig(Motion.MAX_VELOCITY, Motion.MAX_ACCELERATION)
            .setKinematics(Drivetrain.Motion.KINEMATICS);

    private static final Trajectory DEFAULT_TRAJECTORY =
            TrajectoryGenerator.generateTrajectory(
                    new Pose2d(0, 0, new Rotation2d()),
                    List.of(),
                    new Pose2d(1, 0, new Rotation2d()),
                    new TrajectoryConfig(0.1, 0.1)
                            .setKinematics(Drivetrain.Motion.KINEMATICS));

    private TrajectoryLoader() {
    }

    // Function that gets a trajectory from path weaver,
    // but will give a default one if it has an issue
    public static Trajectory getTrajectory(String path) {
        try {
            return TrajectoryUtil.fromPathweaverJson(Constants.DEPLOY_DIRECTORY.resolve(path));
        } catch (IOException e) {
            DriverStation.reportError("Error Opening \"" + path + "\"!", e.getStackTrace());

            System.err.println("Error Opening \"" + path + "\"!");
            System.out.println(e.getStackTrace());

            return DEFAULT_TRAJECTORY;
        }
    }

    // Function that gets multiple trajectories and concatinates them together
    public static Trajectory getTrajectory(String... paths) {
        Trajectory trajectory = getTrajectory(paths[0]);

        for (int i = 1; i < paths.length; ++i) {
            trajectory = trajectory.concatenate(getTrajectory(paths[i]));
        }

        return trajectory;
    }

    public static Trajectory getLine(double distance) {
        return TrajectoryGenerator.generateTrajectory(
            new Pose2d(),
            List.of(),
            new Pose2d(distance, 0, new Rotation2d()),
            MAX_SPEED_TRAJECTORY.setReversed(distance < 0)
        );
    }
}

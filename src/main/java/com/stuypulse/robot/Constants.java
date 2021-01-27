package com.stuypulse.robot;

import java.nio.file.Path;

import com.stuypulse.stuylib.network.SmartBoolean;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;


/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public interface Constants {

    // for file io
    Path DEPLOY_DIRECTORY = Filesystem.getDeployDirectory().toPath();

    interface OnBoardIO {

        double MESSAGE_INTERVAL = 1.0;

    }

    interface Drivetrain {

        SmartBoolean USE_GYROSCOPE = new SmartBoolean("Use Gyroscope for Angle", false);

        double TRACK_WIDTH = 0.141; 
        double DEAD_BAND = 0.1;

        DifferentialDriveKinematics KINEMATICS = new DifferentialDriveKinematics(TRACK_WIDTH);

        SimpleMotorFeedforward MOTOR_FEED_FORWARD = new SimpleMotorFeedforward(
            FeedForward.S, 
            FeedForward.V, 
            FeedForward.A
        );

        interface Ports {
            int LEFT_MOTOR = 0;
            int RIGHT_MOTOR = 1;

            int LEFT_ENCODER_A = 4;
            int LEFT_ENCODER_B = 5;

            int RIGHT_ENCODER_A = 6;
            int RIGHT_ENCODER_B = 7;
        }

        interface Odometry {
            double START_X = 0.0;
            double START_Y = 0.0;

            Rotation2d START_ANG = new Rotation2d(0.0);

            Pose2d START = new Pose2d(START_X, START_Y, START_ANG);
        }

        interface Encoders {
            double COUNTS_PER_REVOLUTION = 1440.0;
            double WHEEL_DIAMETER_METER = 0.070;

            double DISTANCE_PER_PULSE = (Math.PI * WHEEL_DIAMETER_METER) / COUNTS_PER_REVOLUTION;
        }

        interface FeedForward {
            double S = 0.0;
            double V = 10.0;
            double A = 0.0;
        }

        interface TrajectoryConfiguration {
            
            double MAX_VOLTAGE = 10.0;
            
            DifferentialDriveVoltageConstraint VOLTAGE_CONSTRAINT = new DifferentialDriveVoltageConstraint(
                MOTOR_FEED_FORWARD,
                KINEMATICS, 
                MAX_VOLTAGE
            );

            TrajectoryConfig CONFIG = new TrajectoryConfig(
                0.5, // Velocity (m/s)
                1.0 // Acceleration (m/s^2)
            )
            .setKinematics(KINEMATICS)
            .addConstraint(VOLTAGE_CONSTRAINT);
        }

        interface PID {
            double P = 0.5;
            double I = 0;
            double D = 0;
        }

    }

}

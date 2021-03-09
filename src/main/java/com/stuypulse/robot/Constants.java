package com.stuypulse.robot;

import java.nio.file.Path;

import com.stuypulse.stuylib.network.SmartBoolean;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public interface Constants {

    // This is the scale for units, this is used to make the robot simulate another
    // size
    // The romi is about 5 inches across, and edwin is about 30 inches across
    // By setting the robot scale to 1.0 / 5.0, we can simulate a robot the size of
    // edwin
    double ROBOT_SCALE = 1.0 / 5.0;

    // for file io
    Path DEPLOY_DIRECTORY = Filesystem.getDeployDirectory().toPath();

    interface OnBoardIO {
        double MESSAGE_INTERVAL = 1.0;
    }

    interface Drivetrain {

        SmartBoolean USE_GYROSCOPE = new SmartBoolean("Drivetrain/Use Gyroscope", false);

        int SENDABLE_SIGFIGS = 5;

        double LEFT_VOLTAGE_MUL = 1.0;
        double RIGHT_VOLTAGE_MUL = -1.0;

        double TRACK_WIDTH = 0.141 / ROBOT_SCALE;

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
            double WHEEL_DIAMETER_METER = 0.070 / ROBOT_SCALE;

            double DISTANCE_PER_PULSE = (Math.PI * WHEEL_DIAMETER_METER) / COUNTS_PER_REVOLUTION;
        }

        interface Motion {

            DifferentialDriveKinematics KINEMATICS = new DifferentialDriveKinematics(TRACK_WIDTH);

            SimpleMotorFeedforward MOTOR_FEED_FORWARD = new SimpleMotorFeedforward(
                FeedForward.S,
                FeedForward.V, 
                FeedForward.A
            );

            interface FeedForward {
                double S = 0.376;
                double V = 10.00 * ROBOT_SCALE;
                double A = 0.186 * ROBOT_SCALE;
            }

            interface PID {
                double P = 0.0125 * ROBOT_SCALE;
                double I = 0 * ROBOT_SCALE;
                double D = 0 * ROBOT_SCALE;
            }
        }

    }
}
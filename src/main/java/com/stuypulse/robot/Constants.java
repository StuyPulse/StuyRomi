package com.stuypulse.robot;

import java.nio.file.Path;
import edu.wpi.first.wpilibj.Filesystem;


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

        double TRACK_WIDTH = 0.141; // METERS!!!

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
        }

        interface Encoders {
            double COUNTS_PER_REVOLUTION = 1440.0;
            double WHEEL_DIAMETER_METER = 0.070;

            double DISTANCE_PER_PULSE = (Math.PI * WHEEL_DIAMETER_METER) / COUNTS_PER_REVOLUTION;
        }

        // TODO: fill in motion profile here (i think)
        interface FeedForward {
            double S = 0.0;
            double V = 0.0;
            double A = 0.0;
        }

        interface PID {
            double P = 0;
            double I = 0;
            double D = 0;
        }

    }

}

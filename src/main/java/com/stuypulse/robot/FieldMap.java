package com.stuypulse.robot;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;

public interface FieldMap {
    static int getAlphaIndex(char in) {
        // A -> 4, B -> 3, C -> 2, D -> 1, E -> 0
        return 'E' - (Character.toUpperCase(in) - 'A');
    }

    // Number of inches between letters
    double COLUMN_INCHES = 30;
    // Number of inches between rows
    double ROW_INCHES = 30;

    static Translation2d get(String name) {
        if (name.split(":").length > 1)
            return getRange(name);
        else
            return getPoint(name);
    }

    static Translation2d getRange(String name) {
        String[] names = name.split(":");
        double x = 0, y = 0;

        for (String i : names) {
            Translation2d temp = getPoint(i);
            x += temp.getX();
            y += temp.getY();
        }

        return new Translation2d(x / names.length, y / names.length);
    }

    static Translation2d getPoint(String name) {
        name = name.toUpperCase();

        // Assume the string is the right length
        int letter = getAlphaIndex(name.charAt(0)) + 1;
        Double number = Double.parseDouble(name.substring(1));

        return new Translation2d(Units.inchesToMeters(number * ROW_INCHES),
                Units.inchesToMeters(letter * COLUMN_INCHES));
    }

    /**
     * 
     * @param startPoint
     * @param start
     * @param points
     * @param endPoint
     * @param end
     * @param constraints
     * @return
     */
    static Trajectory getTrajectory(
            String startPoint, Rotation2d start, 
            String points, 
            String endPoint, Rotation2d end,
            TrajectoryConfig constraints) {
        return TrajectoryGenerator.generateTrajectory(new Pose2d(get(startPoint), start),

                parsePoints(points),

                new Pose2d(get(endPoint), end),

                constraints);
    }

    static List<Translation2d> parsePoints(String name) {
        String[] a = name.split("\\s+"); // i dont think java has multiline strings but \n worksYTea
        List<Translation2d> output = new ArrayList<>();

        for (int i = 0; i < a.length; i++)
            output.add(get(a[i]));

        return output;
    }
}

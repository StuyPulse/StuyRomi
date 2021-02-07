package com.stuypulse.robot;

import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.util.Units;

import java.util.List;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;

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
        Translation2d output;
        if (name.split(":").length > 1)
            output = getRange(name);
        else
            output = getPoint(name);
        System.out.println(output.getX()+", "+output.getY());
        return output;
    }
    
    private static Translation2d getRange(String name) {
        String[] names = name.split(":");
        Translation2d[] points = new Translation2d[names.length];

        for (int i = 0; i < points.length; i++)
            points[i] = get(names[i]);
        
        return avg(points);
    }

    private static Translation2d getPoint(String name) {
        name = name.toUpperCase();

        // Assume the string is the right length
        int letter = getAlphaIndex(name.charAt(0)) + 1;
        Double number = Double.parseDouble(name.substring(1));

        return new Translation2d(
            Units.inchesToMeters(number * ROW_INCHES),
            Units.inchesToMeters(letter * COLUMN_INCHES)
        );
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
        String startPoint,
        Rotation2d start,
        
        String points,

        String endPoint,
        Rotation2d end,
        
        TrajectoryConfig constraints
    ) {
        return TrajectoryGenerator.generateTrajectory(
            new Pose2d(get(startPoint), start),
            
            parsePoints(points),
            
            new Pose2d(get(endPoint), end),

            constraints
        );
    }

    private static List<Translation2d> parsePoints(String name) {
        String[] a = name.split("\\s+"); // i dont think java has multiline strings but \n worksYTea
        List<Translation2d> output = new ArrayList<>();

        for (int i = 0; i < a.length; i++)
            output.add(get(a[i]));
        
        return output;
    }
    
    static Translation2d avg(Translation2d[] inputs) {
        double x = 0, y = 0;

        for (Translation2d a : inputs) {
            x += a.getX();
            y += a.getY();
        }

        x /= inputs.length;
        y /= inputs.length;

        return new Translation2d(x, y);
    }
}

package com.stuypulse.robot.commands.autos.rr;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.DrivetrainRamseteCommand;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class FourBallCargoAuton extends SequentialCommandGroup {

    public static final double SHOOTER_RING_START = .1;
    public static final double INTAKE_ON = .1;
    public static final double SHOOT = .1;
    public static final double INTAKE_BALL = .1;

    public static final String SHOOT_BALL_PATH = "FourBallCargoAuton/output/ShootBall.wpilib.json";
    public static final String TO_CARGO_BALLS = "FourBallCargoAuton/output/ToCargoBalls.wpilib.json";
    public static final String TO_FIRST_BALL = "FourBallCargoAuton/output/ToFirstBall.wpilib.json";

    

    public FourBallCargoAuton(RobotContainer robot){
        
        addCommands(
            new WaitCommand(SHOOTER_RING_START),

            new WaitCommand(INTAKE_ON)
            
         );
         
        addCommands(
            new DrivetrainRamseteCommand(robot.drivetrain, TO_FIRST_BALL).robotRelative(),
            
            new WaitCommand(SHOOT) 

        );

        addCommands(
            
            new DrivetrainRamseteCommand(robot.drivetrain, TO_CARGO_BALLS).fieldRelative(),
            
            new WaitCommand(INTAKE_BALL) // with interrupt when conveyor is full
        );
        
        addCommands(
            new DrivetrainRamseteCommand(robot.drivetrain, SHOOT_BALL_PATH).fieldRelative(),

            new WaitCommand(SHOOT)
        );
         

         

    }
        
}
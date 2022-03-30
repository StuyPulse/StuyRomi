package com.stuypulse.robot.commands.autos.rr;

import com.stuypulse.robot.commands.DrivetrainRamseteCommand;
import com.stuypulse.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class FourBallCargoAuton extends SequentialCommandGroup {

    public static final double SHOOTER_RING_START = 1;
    public static final double INTAKE_ON = .5;
    public static final double SHOOT = 1.0;
    public static final double INTAKE_BALL = 1.0;

    public static final String SHOOT_BALL_PATH = "deploy/FourBallCargo/ShootBall.wpilib.json";
    public static final String TO_CARGO_BALLS = "deploy/FourBallCargo/ToCargoBalls.wpilib.json";
    public static final String TO_FIRST_BALL = "deploy/FourBallCargo/ToFirstBall.wpilib.json";
    

    public FourBallCargoAuton(Drivetrain drivetrain){
        
        addCommands(
            new WaitCommand(SHOOTER_RING_START),

            new WaitCommand(INTAKE_ON)
            
         );
         
        addCommands(
            new DrivetrainRamseteCommand(drivetrain, TO_FIRST_BALL),
            
            new WaitCommand(SHOOT) 

        );

        addCommands(
            
            new DrivetrainRamseteCommand(drivetrain, TO_CARGO_BALLS),
            
            new WaitCommand(INTAKE_BALL) // with interrupt when conveyor is full
        );
        
        addCommands(
            new DrivetrainRamseteCommand(drivetrain, SHOOT_BALL_PATH),

            new WaitCommand(SHOOT)
        );
         

         

    }
        
}
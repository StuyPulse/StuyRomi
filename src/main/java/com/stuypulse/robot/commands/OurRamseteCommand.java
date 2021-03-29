package com.stuypulse.robot.commands;

import com.stuypulse.robot.Constants;
import com.stuypulse.robot.Constants.Drivetrain.Motion;

import com.stuypulse.robot.subsystems.Drivetrain;
import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.control.PIDController;
import com.stuypulse.stuylib.util.StopWatch;

import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class OurRamseteCommand extends CommandBase {
    
    protected Drivetrain drivetrain;

    protected Trajectory trajectory;

    protected RamseteController follower;
    protected SimpleMotorFeedforward feedforward;

    protected DifferentialDriveKinematics kinematics;

    protected /* Controller */ PIDController leftController;
    protected /* Controller */ PIDController rightController;

    private StopWatch stopwatch;
    private double previousTime;

    private DifferentialDriveWheelSpeeds previousSpeeds;
    
    public OurRamseteCommand(
        Drivetrain drivetrain, 
        Trajectory trajectory, 
        RamseteController follower,
        SimpleMotorFeedforward feedforward,
        DifferentialDriveKinematics kinematics, 
        /* Controller */ PIDController leftController,
        /* Controller */ PIDController rightController
    ) {
        this.drivetrain = drivetrain;
        this.trajectory = trajectory;
        this.feedforward = feedforward;
        this.follower = follower;
        this.kinematics = kinematics;
        this.leftController = leftController;
        this.rightController = rightController;

        this.stopwatch = new StopWatch();
        this.previousTime = 0.0;
        
        this.previousSpeeds = null;

        addRequirements(drivetrain);
    }

    public OurRamseteCommand(
        Drivetrain drivetrain,
        Trajectory trajectory
    ) {
        this(
            drivetrain,
            trajectory,
            new RamseteController(),
            Motion.MOTOR_FEED_FORWARD,
            Motion.KINEMATICS,
            new PIDController(Motion.PID.P, Motion.PID.I, Motion.PID.D),
            new PIDController(Motion.PID.P, Motion.PID.I, Motion.PID.D)
        );
    }

    @Override
    public void initialize() {
        this.previousTime = -1;

        Trajectory.State initial = trajectory.sample(0);
        this.previousSpeeds = kinematics.toWheelSpeeds(
            new ChassisSpeeds(
                initial.velocityMetersPerSecond,
                0,
                initial.curvatureRadPerMeter * initial.velocityMetersPerSecond
            )
        );

        this.stopwatch.reset();

        // This doesn't actually do anything
        this.leftController.reset();
        this.rightController.reset();

    }

    @Override
    public void execute() {
        final double currentTime = stopwatch.getTime();
        final double dt = currentTime - previousTime;

        // First time in the execute loop,
        if (previousTime < 0) {
            drivetrain.tankDriveVolts(0, 0);
            previousTime = currentTime;
            return;
        }

        DifferentialDriveWheelSpeeds targetSpeeds = kinematics.toWheelSpeeds(
            follower.calculate(
                drivetrain.getPose(),
                trajectory.sample(currentTime)
            )
        );
    
        double leftSpeed = targetSpeeds.leftMetersPerSecond;
        double rightSpeed = targetSpeeds.rightMetersPerSecond;

        double leftFeedforward = feedforward.calculate(
            leftSpeed, ((leftSpeed - previousSpeeds.leftMetersPerSecond) / dt)
        );
        double rightFeedforward = feedforward.calculate(
            rightSpeed, ((rightSpeed - previousSpeeds.rightMetersPerSecond) / dt)
        );

        DifferentialDriveWheelSpeeds currentSpeeds = drivetrain.getWheelSpeeds();       
        double leftOutput = leftFeedforward + leftController.update(
            leftSpeed - currentSpeeds.leftMetersPerSecond
        );
        double rightOutput = rightFeedforward + rightController.update(
            rightSpeed - currentSpeeds.rightMetersPerSecond
        );

        drivetrain.tankDriveVolts(leftOutput, rightOutput);
        previousSpeeds = targetSpeeds;
        previousTime = currentTime;
    }

    @Override
    public boolean isFinished() {
        return stopwatch.getTime() > trajectory.getTotalTimeSeconds();
    }

}

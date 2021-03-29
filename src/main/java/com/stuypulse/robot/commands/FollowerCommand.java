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

/**
 * This is a remake of the edu.wpi.first.wpilibj2.command.RamseteCommand,
 * but easier to use with child classes for testing, research, etc.
 */
public class FollowerCommand extends CommandBase {
    
    // Helper class for storing voltages
    private static class DifferentialDriveVoltages {

        public final double leftVoltage, rightVoltage;

        public DifferentialDriveVoltages(double leftVoltage, double rightVoltage) {
            this.leftVoltage = leftVoltage;
            this.rightVoltage = rightVoltage;
        }
        
    }

    /****************
     * CLASS FIELDS *
     ****************/

    // Drivetrain that follows the path
    protected Drivetrain drivetrain;

    // Trajectory to follow, supplies velocities
    protected Trajectory trajectory;

    // Calculates fwd, angular velocity based on a goal position
    protected RamseteController follower;

    // Converts ideal velocities into wheel speeds
    protected DifferentialDriveKinematics kinematics;

    // Converts velocities into voltages
    protected SimpleMotorFeedforward feedforward;

    // Used to fix up voltages based on the setpoint 
    // and what the drivetrain encoders read
    protected Controller leftController;
    protected Controller rightController;

    // Stopwatch is used to measure time, sample trajectory
    protected StopWatch stopwatch;

    // Previous time is used to calculate dT
    // dT is used to calculate acceleration
    private double previousTime;

    // Previous speeds are used to calculate acceleration
    private DifferentialDriveWheelSpeeds previousSpeeds;
    
    /****************
     * CONSTRUCTORS *
     ****************/

    public FollowerCommand(
        Drivetrain drivetrain, 
        Trajectory trajectory, 
        RamseteController follower,
        SimpleMotorFeedforward feedforward,
        DifferentialDriveKinematics kinematics, 
        Controller leftController,
        Controller rightController
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

    public FollowerCommand(
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


    /***************************
     * COMMAND BASED FUNCTIONS *
     ***************************/

    @Override
    public void initialize() {
        // Initialize the previous time
        this.previousTime = -1;

        // Initialize previous speeds
        Trajectory.State initial = trajectory.sample(0);
        this.previousSpeeds = kinematics.toWheelSpeeds(
            new ChassisSpeeds(
                initial.velocityMetersPerSecond,
                0,
                initial.curvatureRadPerMeter * initial.velocityMetersPerSecond
            )
        );

        // Reset timer
        this.stopwatch.reset();
    }

    @Override
    public void execute() {
        // Get the current time and dT
        final double currentTime = stopwatch.getTime();
        final double dt = currentTime - previousTime;

        // If first time in the execute loop, do nothing
        if (previousTime < 0) {
            drivetrain.tankDriveVolts(0, 0);
            previousTime = currentTime;
            return;
        }

        // Get target wheel speeds (with the ramsete controller)
        DifferentialDriveWheelSpeeds targetSpeeds = kinematics.toWheelSpeeds(
            follower.calculate(
                drivetrain.getPose(),
                trajectory.sample(currentTime)
            )
        );
    
        // Get the voltages for each wheel
        DifferentialDriveVoltages voltages = getVoltages(
            targetSpeeds,
            dt
        );

        // Use voltage outputs
        drivetrain.tankDriveVolts(
            voltages.leftVoltage, 
            voltages.rightVoltage
        );

        // Update previous speeds and time
        previousSpeeds = targetSpeeds;
        previousTime = currentTime;
    }

    @Override
    public boolean isFinished() {
        return stopwatch.getTime() > trajectory.getTotalTimeSeconds();
    }

    /********************
     * HELPER FUNCTIONS *
     ********************/

    private DifferentialDriveVoltages getVoltages(
        DifferentialDriveWheelSpeeds target, 
        double dt
    ) {

        // Left, right desired velocity
        double left = target.leftMetersPerSecond;
        double right = target.rightMetersPerSecond;
        
        // Left, right (raw) voltage
        double leftVoltage = feedforward.calculate(
            // velocity    
            left,
            // acceleration
            (left - previousSpeeds.leftMetersPerSecond) / dt
        );
        double rightVoltage = feedforward.calculate(
            // velocity    
            right,
            // acceleration
            (right - previousSpeeds.rightMetersPerSecond) / dt
        );

        // Left, right voltages PID corrected
        DifferentialDriveWheelSpeeds measurement = drivetrain.getWheelSpeeds();        
        leftVoltage += leftController.update(
            left - measurement.leftMetersPerSecond
        );
        rightVoltage += rightController.update(
            right - measurement.rightMetersPerSecond
        );

        return new DifferentialDriveVoltages(
            leftVoltage,
            rightVoltage
        );
    }


}

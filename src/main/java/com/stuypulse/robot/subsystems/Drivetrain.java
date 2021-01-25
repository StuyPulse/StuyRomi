// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.stuypulse.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.stuypulse.robot.subsystems.sensors.RomiGyro;

import static com.stuypulse.robot.Constants.Drivetrain.*;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;

import com.stuypulse.stuylib.math.Angle;

import edu.wpi.first.wpilibj.geometry.*;

public class Drivetrain extends SubsystemBase {

    // Motors
    private final Spark leftMotor;
    private final Spark rightMotor;

    private final DifferentialDrive drivetrain;

    // Encoders
    private final Encoder leftEncoder;
    private final Encoder rightEncoder;
    
    // Sensors
    private final RomiGyro gyro;

    // Odemetery
    private DifferentialDriveOdometry odometry;
    private DifferentialDriveKinematics kinematics;

    public Drivetrain() {

        // Gyro
        gyro = new RomiGyro();
        resetGyro();

        // Setup motors and drivetrain
        leftMotor = new Spark(Ports.LEFT_MOTOR);
        rightMotor = new Spark(Ports.RIGHT_MOTOR);

        drivetrain = new DifferentialDrive(leftMotor, rightMotor);

        // Setup encoders
        leftEncoder = new Encoder(Ports.LEFT_ENCODER_A, Ports.LEFT_ENCODER_B);
        rightEncoder = new Encoder(Ports.RIGHT_ENCODER_A, Ports.RIGHT_ENCODER_B);

        leftEncoder.setDistancePerPulse(Encoders.DISTANCE_PER_PULSE);
        rightEncoder.setDistancePerPulse(Encoders.DISTANCE_PER_PULSE);

        resetEncoders();

        // Odemetery

        odometry = new DifferentialDriveOdometry(
            this.getRotation2d(), 
            new Pose2d(Odometry.START_X, Odometry.START_Y, new Rotation2d())
        );

        kinematics = new DifferentialDriveKinematics(TRACK_WIDTH);
    }

    /*********************
     * DRIVING FUNCTIONS *
     *********************/

    public void tankDrive(double leftSpeed, double rightSpeed) {
        drivetrain.tankDrive(leftSpeed, rightSpeed);
    }

    public void arcadeDrive(double xaxisSpeed, double zaxisRotate) {
        drivetrain.arcadeDrive(xaxisSpeed, zaxisRotate);
    }

    public void tankDriveVolts(double leftVolts, double rightVolts) {
        leftMotor.setVoltage(leftVolts);
        rightMotor.setVoltage(rightVolts);
        drivetrain.feed();
    }

    public void stop() {
        tankDriveVolts(0, 0);
    }

    /*********************
     * ENCODER FUNCTIONS *
     *********************/

    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public int getRawLeftEncoder() {
        return leftEncoder.get();
    }

    public int getRawRightEncoder() {
        return rightEncoder.get();
    }

    public double getLeftDistance() {
        return leftEncoder.getDistance();
    }

    public double getRightDistance() {
        return rightEncoder.getDistance();
    }

    public double getLeftVelocity() {
        return leftEncoder.getRate();
    }

    public double getRightVelocity() {
        return rightEncoder.getRate();
    }

    public double getVelocity() {
        return (getLeftVelocity() + getRightVelocity()) / 2.0;
    }

    public double getDistance() {
        return (getLeftDistance() + getRightDistance()) / 2.0;
    }

    /******************
     * GYRO FUNCTIONS *
     ******************/

    public Angle getAngleRoll() {
        return Angle.fromDegrees(gyro.getAngleX());
    }

    public Angle getAnglePitch() {
        return Angle.fromDegrees(gyro.getAngleY());
    }

    public Angle getAngleYaw() {
        return Angle.fromDegrees(gyro.getAngleZ());
    }

    public void resetGyro() {
        gyro.reset();
    }

    /**********************
     * ODOMETER FUNCTIONS *
     **********************/

    public void reset() {
        resetEncoders();
        resetGyro();
        odometry.resetPosition(
            new Pose2d(), 
            new Rotation2d()
        );
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(
            getLeftVelocity(),
            getRightVelocity()
        );
    }

    public Rotation2d getRotation2d() {
        // return a negated value IF gyro returns positive value
        // as the robot turns clockwise
        return new Rotation2d(
            -this.getAngleYaw().toRadians()
        );
    }

    public DifferentialDriveKinematics getKinematics() {
        return kinematics;
    }

    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    @Override
    public void periodic() {
        
        odometry.update(
            this.getRotation2d(),
            this.getLeftDistance(),
            this.getRightDistance()
        );

    }

}
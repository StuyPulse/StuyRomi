// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.stuypulse.robot.subsystems;

import static com.stuypulse.robot.Constants.Drivetrain.*;

import com.stuypulse.stuylib.math.Angle;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.romi.RomiGyro;


public class Drivetrain extends SubsystemBase {

    // Motors
    private final Spark leftMotor;
    private final Spark rightMotor;

    private final DifferentialDrive drivetrain;

    // Encoders
    private final Encoder leftEncoder;
    private final Encoder rightEncoder;

    // Gyro
    private final RomiGyro gyro;

    // Odemetery
    private DifferentialDriveOdometry odometry;

    // Field Widget
    private Field2d field;

    public Drivetrain() {

        // Setup motors and drivetrain
        leftMotor = new Spark(Ports.LEFT_MOTOR);
        rightMotor = new Spark(Ports.RIGHT_MOTOR);

        rightMotor.setInverted(true);

        drivetrain = new DifferentialDrive(leftMotor, rightMotor);

        // Setup encoders
        leftEncoder = new Encoder(Ports.LEFT_ENCODER_A, Ports.LEFT_ENCODER_B);
        rightEncoder = new Encoder(Ports.RIGHT_ENCODER_A, Ports.RIGHT_ENCODER_B);

        leftEncoder.setDistancePerPulse(Encoders.DISTANCE_PER_PULSE);
        rightEncoder.setDistancePerPulse(Encoders.DISTANCE_PER_PULSE);

        resetEncoders();

        // Gyro
        gyro = new RomiGyro();
        resetGyro();

        // Odemetery
        odometry = new DifferentialDriveOdometry(Odometry.START_ANG, Odometry.START);

        // Field Widget
        field = new Field2d();

        // Add children objects
        addChild("Left Motor", leftMotor);
        addChild("Right Motor", rightMotor);
        addChild("Left Encoder", leftEncoder);
        addChild("Right Encoder", rightEncoder);
        addChild("Differential Drive", drivetrain);
    }

    /*********************
     * VOLTAGE FUNCTIONS *
     *********************/

    // Gets the current voltage of the robot, which affects speed
    public double getBatteryVoltage() {
        return RobotController.getBatteryVoltage();
    }

    // Gets the last set voltage for left motor
    public double getLeftVoltage() {
        // We divide by the voltage multiplier here because
        // we multiply by it when setting the motor
        return (getBatteryVoltage() * leftMotor.get());
    }

    // Gets the last set voltage for right motor
    public double getRightVoltage() {
        return (getBatteryVoltage() * rightMotor.get());
    }

    /*********************
     * ENCODER FUNCTIONS *
     *********************/

    /** Left Side **/
    // Get raw encoder count of the left encoder
    public int getRawLeftEncoder() {
        return leftEncoder.get();
    }

    // The distance in meters that the wheel has traveled
    public double getLeftDistance() {
        return leftEncoder.getDistance();
    }

    // The current speed of the left wheel in meters / sec
    public double getLeftVelocity() {
        return leftEncoder.getRate();
    }

    /** Right Side **/
    // Get raw encoder count of the left encoder
    public int getRawRightEncoder() {
        return rightEncoder.get();
    }

    // The distance in meters that the wheel has traveled
    public double getRightDistance() {
        return rightEncoder.getDistance();
    }

    // The current speed of the left wheel in meters / sec
    public double getRightVelocity() {
        return rightEncoder.getRate();
    }

    /** Both Sides **/
    // Current distance in meters that the robot has traveled
    public double getDistance() {
        return (getLeftDistance() + getRightDistance()) / 2.0;
    }

    // The current speed of the robot in meters / sec
    public double getVelocity() {
        return (getLeftVelocity() + getRightVelocity()) / 2.0;
    }

    // Angle of the robot based on the difference in encoder values
    public Angle getEncoderAngle() {
        double diffMeters = getRightDistance() - getLeftDistance();
        return Angle.fromRadians(diffMeters / TRACK_WIDTH);
    }

    /** Reset **/
    private void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    /******************
     * GYRO FUNCTIONS *
     ******************/

    public Angle getAngleRoll() {
        return Angle.fromDegrees(-gyro.getAngleX());
    }

    public Angle getAnglePitch() {
        return Angle.fromDegrees(gyro.getAngleY());
    }

    public Angle getAngleYaw() {
        return Angle.fromDegrees(-gyro.getAngleZ());
    }

    public Angle getAngle() {
        if (USE_GYROSCOPE.get()) {
            return getAngleYaw();
        } else {
            return getEncoderAngle();
        }
    }

    private void resetGyro() {
        gyro.reset();
    }

    /**********************
     * ODOMETER FUNCTIONS *
     **********************/

    private void updateOdometry() {
        odometry.update(this.getRotation2d(), this.getLeftDistance(), this.getRightDistance());
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(getLeftVelocity(), getRightVelocity());
    }

    public Rotation2d getRotation2d() {
        return getAngle().getRotation2d();
    }

    public Pose2d getPose() {
        updateOdometry();
        return odometry.getPoseMeters();
    }

    @Override
    public void periodic() {
        updateOdometry();
        field.setRobotPose(getPose());
        
        // Smart Dashboard Information
        SmartDashboard.putData("Drivetrain/Field", field);
        SmartDashboard.putNumber("Drivetrain/Odometer X Position (m)", getPose().getX());
        SmartDashboard.putNumber("Drivetrain/Odometer Y Position (m)", getPose().getY());
        SmartDashboard.putNumber("Drivetrain/Odometer Rotation (deg)", getPose().getRotation().getDegrees());
        
        SmartDashboard.putNumber("Drivetrain/Motor Voltage Left (V)", getLeftVoltage());
        SmartDashboard.putNumber("Drivetrain/Motor Voltage Right (V)", getRightVoltage());

        SmartDashboard.putNumber("Drivetrain/Distance Traveled (m)", getDistance());
        SmartDashboard.putNumber("Drivetrain/Distance Traveled Left (m)", getLeftDistance());
        SmartDashboard.putNumber("Drivetrain/Distance Traveled Right (m)", getRightDistance());
        
        SmartDashboard.putNumber("Drivetrain/Velocity (m per s)", getVelocity());
        SmartDashboard.putNumber("Drivetrain/Velocity Left (m per s)", getLeftVelocity());
        SmartDashboard.putNumber("Drivetrain/Velocity Right (m per s)", getRightVelocity());

        SmartDashboard.putNumber("Drivetrain/Gyroscope (deg)", getAngle().toDegrees());
    }

    private void resetOdometer(Pose2d startPose2d) {
        odometry.resetPosition(startPose2d, new Rotation2d());
    }

    // Resets all encoders / gyroscopes to 0
    public void reset(Pose2d startPose2d) {
        resetEncoders();
        resetGyro();

        // Must Be Called Last
        resetOdometer(startPose2d);
    }

    public void reset() {
        reset(Odometry.START);
    }

    /*********************
     * DRIVING FUNCTIONS *
     *********************/

    public void tankDrive(double leftSpeed, double rightSpeed) {
        drivetrain.tankDrive(leftSpeed, rightSpeed, false);
    }

    public void arcadeDrive(double xaxisSpeed, double zaxisRotate) {
        drivetrain.arcadeDrive(xaxisSpeed, zaxisRotate, false);
    }

    public void curvatureDrive(double xaxisSpeed, double zaxisRotate, boolean isQuickTurn) {
        drivetrain.curvatureDrive(xaxisSpeed, zaxisRotate, isQuickTurn);
    }

    public void tankDriveVolts(double leftVolts, double rightVolts) {
        leftMotor.setVoltage(leftVolts);
        rightMotor.setVoltage(rightVolts);
        drivetrain.feed();
    }

    public void stop() {
        drivetrain.stopMotor();
    }
}
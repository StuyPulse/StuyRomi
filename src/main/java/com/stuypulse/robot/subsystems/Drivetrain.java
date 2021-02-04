// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.stuypulse.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.stuypulse.robot.subsystems.sensors.RomiGyro;

import static com.stuypulse.robot.Constants.Drivetrain.*;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.stuypulse.stuylib.math.Angle;
import com.stuypulse.stuylib.math.SLMath;

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

    // Field Widget
    private Field2d field; 

    public Drivetrain() {

        // Setup motors and drivetrain
        leftMotor = new Spark(Ports.LEFT_MOTOR);
        rightMotor = new Spark(Ports.RIGHT_MOTOR);

        drivetrain = new DifferentialDrive(leftMotor, rightMotor);

        drivetrain.setDeadband(DEAD_BAND);

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
        updateField();
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
        return (getBatteryVoltage() * leftMotor.get()) / LEFT_VOLTAGE_MUL;
    }

    // Gets the last set voltage for right motor
    public double getRightVoltage() {
        return (getBatteryVoltage() * rightMotor.get()) / RIGHT_VOLTAGE_MUL;
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
        if(USE_GYROSCOPE.get()) {
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

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(
            getLeftVelocity(),
            getRightVelocity()
        );
    }

    public Rotation2d getRotation2d() {
        return getAngle().getRotation2d();
    }

    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    private void updateField() {
        field.setRobotPose(getPose());
        SmartDashboard.putData("Field", field);
    }

    @Override
    public void periodic() {
        odometry.update(
            this.getRotation2d(),
            this.getLeftDistance(),
            this.getRightDistance()
        );
        updateField();
    }

    private void resetOdometer() {
        odometry.resetPosition(Odometry.START, Odometry.START_ANG);
        updateField();
    }

    // Resets all encoders / gyroscopes to 0
    public void reset() {
        resetEncoders();
        resetGyro();
        resetOdometer();
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
        leftMotor.setVoltage(leftVolts * LEFT_VOLTAGE_MUL);
        rightMotor.setVoltage(rightVolts * RIGHT_VOLTAGE_MUL);
        drivetrain.feed();
    }

    public void stop() {
        drivetrain.stopMotor();
    }

    /****************************
     * SMART DASHBOARD SENDABLE *
     ****************************/

    // Rounds a double to a certain number of sigfigs to avoid clutter
    private double round(double n) {
        return SLMath.round(n, SENDABLE_SIGFIGS);
    }

    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);

        // Odometry Functions
        // You have to call getPose() inside the lambda or else it never updates
        builder.addStringProperty("Odometer X: ", 
            () -> round(getPose().getX()) + "m", 
            (x) -> {});

        builder.addStringProperty("Odometer Y: ", 
            () -> round(getPose().getY()) + "m", 
            (x) -> {});

        builder.addStringProperty("Odometer Rotation: ", 
            () -> round(getPose().getRotation().getDegrees()) + "deg", 
            (x) -> {});

        // Left and Right motor functions
        builder.addStringProperty("Distance Left: ", 
            () -> round(getLeftDistance()) + "m", 
            (x) -> {});

        builder.addStringProperty("Distance Right: ", 
            () -> round(getRightDistance()) + "m", 
            (x) -> {});

        builder.addStringProperty("Velocity Left: ", 
            () -> round(getLeftVelocity()) + "m", 
            (x) -> {});

        builder.addStringProperty("Velocity Right: ", 
            () -> round(getRightVelocity()) + "m", 
            (x) -> {});

        builder.addStringProperty("Voltage Left: ", 
            () -> round(getLeftVoltage()) + "V", 
            (x) -> {});

        builder.addStringProperty("Voltage Right: ", 
            () -> round(getRightVoltage()) + "V", 
            (x) -> {});
    }

}
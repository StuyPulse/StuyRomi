// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.stuypulse.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static com.stuypulse.robot.Constants.Drivetrain.*;

// TODO: Implement the drivetrain subsystem
//      - Required Functions
//          - tankDrive()
//          - arcadeDrive()
//          - stop()

public class Drivetrain extends SubsystemBase {
    private final Spark leftMotor;
    private final Spark rightMotor;

    private final DifferentialDrive drivetrain;

    public Drivetrain() {
        leftMotor = new Spark(Ports.LEFT_MOTOR);
        rightMotor = new Spark(Ports.RIGHT_MOTOR);
        drivetrain = new DifferentialDrive(leftMotor, rightMotor);
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        drivetrain.tankDrive(leftSpeed, rightSpeed);
    }

    public void arcadeDrive(double speed, double rotation) {
        drivetrain.arcadeDrive(speed, rotation);
    }

    public void stop() {
        drivetrain.stopMotor();
    }
}
//idk what my group did but well done
//thank you anthony for carrying
// HARD CARRY
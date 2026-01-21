// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.Odometry;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;



public class OdometrySubsystem extends SubsystemBase {
  /** Creates a new OdometrySubsystem. */
  Encoder leftEncoder = new Encoder(7, 6);
  Encoder rightEncoder = new Encoder(8, 9);
  ADXRS450_Gyro gyro = new ADXRS450_Gyro();
  DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(gyro.getRotation2d(),
   leftEncoder.getDistance(), rightEncoder.getDistance());

  public void log(String logs) {
    System.out.println(logs);
  }

  public OdometrySubsystem() {
    leftEncoder.setDistancePerPulse(Constants.wheelCircumference / 1024); // meters per pulse
    rightEncoder.setDistancePerPulse(Constants.wheelCircumference / 1024); // meters per pulse
    gyro.calibrate();
  }

  // public double getDistance() {
  // leftEncoder.
  // }

  public static enum Side {
    LEFT,
    RIGHT
  }

  public int getRawEncoder(Side side) {
    Encoder encoder = side == Side.RIGHT ? leftEncoder : rightEncoder;
    return encoder.getRaw();
  }

  public double getGyroAngle() {
    return gyro.getAngle();
  }



  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    odometry.update(gyro.getRotation2d(), leftEncoder.getDistance(), rightEncoder.getDistance());
  }

  public Pose2d logPose() {
    return odometry.getPoseMeters();
  }
}

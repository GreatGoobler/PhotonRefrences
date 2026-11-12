// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import java.lang.annotation.Target;

import org.photonvision.PhotonCamera;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class photonVision extends SubsystemBase {

  PhotonCamera frontlimelight = new PhotonCamera("Front_Limelight");

  public static final CommandXboxController m_DriverController = new CommandXboxController(0);

  private double PhotonMaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

  double apriltagYaw = 0;
  boolean apriltagVisible;
  double turnConstant = 0.005;
  double turnOffset = 0;

  public static double Throttle;
  public static double Turn;

  public photonVision() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler 
    var results = frontlimelight.getAllUnreadResults();
    if (results.isEmpty()) {
      apriltagVisible = false;
      apriltagYaw = 0;
    }
    if (!results.isEmpty()) {
      var result = results.get(results.size() - 1);
      if (result.hasTargets()) {
        for (var apriltag : result.getTargets()) {
          if (apriltag.getFiducialId() == 1) {
            apriltagYaw = apriltag.getYaw();
            apriltagVisible = true;
          }
        }
      }
    }


    if (apriltagVisible == true && m_DriverController.rightTrigger().getAsBoolean() == true) {
      Turn = (apriltagYaw - turnOffset) * turnConstant * PhotonMaxAngularRate;
    } else {
      Turn = MathUtil.applyDeadband(m_DriverController.getRightX(), 0.08) * PhotonMaxAngularRate * 0.5;
      Throttle = MathUtil.applyDeadband(m_DriverController.getLeftY(), 0.08) * PhotonMaxAngularRate * 0.5;
    }

      }
    }
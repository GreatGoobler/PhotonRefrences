// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import org.photonvision.PhotonCamera;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class photonVision extends SubsystemBase {

  PhotonCamera frontlimelight = new PhotonCamera("Front_Limelight");

  public static final CommandXboxController m_DriverController = new CommandXboxController(0);

  private double PhotonMaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

  public static double Throttle;
  public static double Turn;

  public photonVision() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler 
        Turn = m_DriverController.getRightX() * PhotonMaxAngularRate * 0.5;
        Throttle = m_DriverController.getLeftY() * PhotonMaxAngularRate * 0.5;

      }
    }
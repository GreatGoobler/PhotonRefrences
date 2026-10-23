// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.lib.Constants;
import frc.robot.subsystems.photonVision;


public class DriveBase extends SubsystemBase {

  photonVision m_PhotonVision = new photonVision();
  
  /*Right side motors */
  //top right motor
  WPI_TalonSRX m_TopRightMotor = new WPI_TalonSRX(Constants.ConstantsMotorIds.kTopRightMotorID);
  //bottom right motor
  WPI_TalonSRX m_BottomRightMotor = new WPI_TalonSRX(Constants.ConstantsMotorIds.kBottomRightMotorID);
  
  /*Left side motors*/
  //top left motor
  WPI_TalonSRX m_TopLeftMotor = new WPI_TalonSRX(Constants.ConstantsMotorIds.kTopLeftMotorID);
  //bottom left motor
  WPI_TalonSRX m_BottomLeftMotor = new WPI_TalonSRX(Constants.ConstantsMotorIds.kBottomLeftMotorID);
  
  public DifferentialDrive m_Drive;

  public DriveBase() {


    //front leader motors
    m_Drive = new DifferentialDrive(m_TopLeftMotor, m_TopRightMotor);

    //follows the front left motor
    m_BottomLeftMotor.follow(m_TopLeftMotor);
    //follows the front Right motor
    m_BottomRightMotor.follow(m_TopRightMotor);
    //inverts motors
    m_TopLeftMotor.setInverted(true);
    m_BottomLeftMotor.setInverted(true);
  
  }

  private void curvDrive() {
   //set speed of motors to the imput of the controllersticks inside of photonVision
    m_Drive.curvatureDrive(photonVision.Throttle, photonVision.Turn, true);
  }


  @Override
  public void periodic() {
    curvDrive();
    // This method will be called once per scheduler run
  }
}

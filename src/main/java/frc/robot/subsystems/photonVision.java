// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import org.photonvision.PhotonCamera;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.math.MathUtil;

public class photonVision extends SubsystemBase {

  //Intantiates our photon camera and gives it the name "Front_Limelight"
  PhotonCamera frontlimelight = new PhotonCamera("Front_Limelight");

  //Instantiates our controller for port 0
  public static final CommandXboxController m_DriverController = new CommandXboxController(0);

  //double used in motor input calculations (limits the speed)
  private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

  //A number telling us the target apriltags horizontal distance from the center of the screen
  double apriltagYaw = 0;
  //is it visible yes or no?
  boolean apriltagVisible;
  //Tuned constant for TankBot, limits the speed at which it turns to prevent under/overshooting, must be tuned for other robots
  double turnConstant = 0.008;
  //offset in case camera is off by a little bit
  double turnOffset = 0;

  /*These turn and throttle doubles are created here rather than typically in the DriveBase because
   they will be changing based on photon data as well as controller input */
  //forwards and backwards movement for a tankbot
  public static double Throttle;
  //turn movement for a tank bot
  public static double Turn;

  public photonVision() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler 

    //get all unread frames from the camera
    var results = frontlimelight.getAllUnreadResults();

    //if its empty then ->
    if (results.isEmpty()) {
      //variables set like this so that the robot doesn't keep turning even when it doesn't see a tag
      apriltagVisible = false;
      apriltagYaw = 0;
    }

    //if results ISNT empty (!)
    if (!results.isEmpty()) {
      //create a new variable acting as our individual frames
      var result = results.get(results.size() - 1);
      //if our frame has a target (apriltag) then ->
      if (result.hasTargets()) {
        for (var apriltag : result.getTargets()) {
          //find if our apriltag is the ID we want, then ->
          if (apriltag.getFiducialId() == 1) {
            //find its yaw value
            apriltagYaw = apriltag.getYaw();
            //and set it true
            apriltagVisible = true;
          }
        }
      }
    }


    //if our apriltag is visible AND the controllers right trigger button is pressed then ->
    if (apriltagVisible == true && m_DriverController.rightTrigger().getAsBoolean() == true) {
      //Math finds our number for us
      Turn = (apriltagYaw - turnOffset) * turnConstant * MaxAngularRate;

      //if either of the two requirements above are not met then ->
    } else {
      /*Set our turn and throttle to our controllers inputs, and add a deadband to said controllers */
      //the 0.5 being multiplied at the end is personal prefrence as it slows down the robot making it less jerky, may be different for other robots
      Turn = MathUtil.applyDeadband(m_DriverController.getRightX(), 0.08) * MaxAngularRate * 0.5;
      Throttle = MathUtil.applyDeadband(m_DriverController.getLeftY(), 0.08) * MaxAngularRate * 0.5;
    }
  }

  //Be sure to check DriveBase.java for how to impliment the Turn and Throttle variables into the acutal motor input
}
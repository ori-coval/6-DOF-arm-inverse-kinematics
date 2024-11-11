// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;

/**
 * Do NOT add any static variables to this class, or any initialization at all.
 * Unless you know what
 * you are doing, do not modify this file except to change the parameter class
 * to the startRobot
 * call.
 */
public final class ProjectIn2D {
  private ProjectIn2D() {
  }

  public static void main(String[] args) {
    // Step 1: Define the initial 3D pose
    Pose3d initialPose = new Pose3d(1.0, 2.0, 3.0, new Rotation3d(0, 0, Math.toRadians(45))); // Example pose

    // Step 3: Compute the angle between the vector (x, y) and the X-axis (using
    // atan2)
    double angle = Math.atan2(initialPose.getY(), initialPose.getX()); // This gives us the angle in radians between the X-axis and the vector

    Pose3d rotatedInitilPose3d = initialPose.rotateBy(new Rotation3d(0, 0, -angle));

    Pose2d projectedPose2d = new Pose2d(rotatedInitilPose3d.getX(), rotatedInitilPose3d.getZ(),
        new Rotation2d(rotatedInitilPose3d.getRotation().getY()));

    // Output the projected 2D pose
    System.out.println("Projected 2D Pose: " + projectedPose2d);
  }
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;

/** Add your docs here. */
public class finalIK {
        
        //TODO: devide to functions
        public static void main(String[] args) {
                // Define the initial pose
                Pose3d finalPose = new Pose3d(
                                1.0, 2.0, 3.0,
                                new Rotation3d(
                                                Math.toRadians(0), // roll
                                                Math.toRadians(0), // pitch
                                                Math.toRadians(0) // yaw
                                ));


                /**
                 * use the length of end effector to third arm and the final pose to find the
                 * pose of the end of the third arm
                 * 
                 */

                // Get the rotation of the fianl pose
                Rotation3d finalPoseRotation = finalPose.getRotation();

                // Calculate the direction vector using all three components
                double forwardX = Math.cos(finalPoseRotation.getZ()) * Math.cos(finalPoseRotation.getY()); // Combine
                                                                                                           // yaw and
                                                                                                           // pitch for
                                                                                                           // X
                                                                                                           // direction
                double forwardY = Math.sin(finalPoseRotation.getZ()) * Math.cos(finalPoseRotation.getY()); // Combine
                                                                                                           // yaw and
                                                                                                           // pitch for
                                                                                                           // Y
                                                                                                           // direction
                double forwardZ = Math.sin(finalPoseRotation.getY()); // Use pitch for Z direction

                // Create a vector that is in the length of the forth arm in the direction of
                // the rotation
                double displacementX = forwardX * Constants.LENGTH_OF_END_EFFECTOR_TO_THIRD_ARM;
                double displacementY = forwardY * Constants.LENGTH_OF_END_EFFECTOR_TO_THIRD_ARM;
                double displacementZ = forwardZ * Constants.LENGTH_OF_END_EFFECTOR_TO_THIRD_ARM;

                // Create the new translation for the pose
                Translation3d endOfThirdArmTranslation = finalPose.getTranslation()
                                .plus(new Translation3d(displacementX, displacementY, displacementZ));

                // Return a new Pose3d with the updated translation (same rotation)
                Pose3d endOfThirdArmPose = new Pose3d(endOfThirdArmTranslation, finalPose.getRotation());
                

                /**
                 * project the third arm translation to a 2d plane so i can be used for IK of
                 * the 3 arms
                 */

                double _baseAngle = Math.atan2(endOfThirdArmPose.getY(), endOfThirdArmPose.getX());

                Pose3d rotatedEndOfThirdArm = endOfThirdArmPose.rotateBy(new Rotation3d(0, 0, -_baseAngle));

                Pose2d endOfThirdArmPosition_2D = new Pose2d(rotatedEndOfThirdArm.getX(), rotatedEndOfThirdArm.getZ(),
                                new Rotation2d(rotatedEndOfThirdArm.getRotation().getY()));
                                
                double _forthArmAngle = rotatedEndOfThirdArm.getRotation().getX();


                /**
                 * simple 3 arm IK
                 */
                Vector2D arm3 = Vector2D.toCartesian(Constants.ARM3_LENGTH,
                                endOfThirdArmPosition_2D.getRotation().getRadians());

                Vector2D baseToArm2Vector = Vector2D.subtract(
                                new Vector2D(endOfThirdArmPosition_2D.getX(), endOfThirdArmPosition_2D.getY()),
                                arm3);

                double arm1TriangleAngle = Math.toDegrees(Math
                                .acos((Constants.ARM1_LENGTH * Constants.ARM1_LENGTH + baseToArm2Vector.getLengthSq()
                                                - Constants.ARM2_LENGTH * Constants.ARM2_LENGTH)
                                                / (2 * Constants.ARM1_LENGTH * baseToArm2Vector.getLength())));

                double arm2TriangleAngle = Math.toDegrees(Math.asin(
                                (baseToArm2Vector.getLength() * Math.sin(Math.toRadians(arm1TriangleAngle)))
                                                / Constants.ARM2_LENGTH));

                double _arm1Angle = wrapAngle(arm1TriangleAngle + Math.toDegrees(baseToArm2Vector.getAngle()));

                double _arm2Angle = wrapAngle(90 - arm2TriangleAngle);

                // TODO: check if endOfThirdArmPosition_2D rotation is the same as in the old
                // code
                double _arm3Angle = wrapAngle(endOfThirdArmPosition_2D.getRotation().getDegrees()
                                - (180 - (arm2TriangleAngle - _arm1Angle)) - 90);
        }

        public static final class Constants {
                public static final double LENGTH_OF_END_EFFECTOR_TO_THIRD_ARM = 0.5;

                public static final double ARM1_LENGTH = 15;
                public static final double ARM2_LENGTH = 10;
                public static final double ARM3_LENGTH = 10;
        }

        public static double wrapAngle(double angle) {
                double result = angle % 360.0;
                if (result < 0) {
                        result += 360;
                }
                return result;
        }
}

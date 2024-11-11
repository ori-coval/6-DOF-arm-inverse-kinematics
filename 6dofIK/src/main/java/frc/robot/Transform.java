package frc.robot;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;

public class Transform {

    public static final double TRANSFORM_LENGTH = 0.7;

    public static void main(String[] args) {
        // Example Pose3d, assuming it has some rotation (e.g., facing 45 degrees in the
        // XY-plane and slightly pitched)
        Pose3d initialPose = new Pose3d(0, 0, 0,
                new Rotation3d(Math.toRadians(0), Math.toRadians(45), Math.toRadians(0))); // Some rotation

        // Get the rotation of the fianl pose
        Rotation3d finalPoseRotation = initialPose.getRotation();

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
        double displacementX = forwardX * TRANSFORM_LENGTH;
        double displacementY = forwardY * TRANSFORM_LENGTH;
        double displacementZ = forwardZ * TRANSFORM_LENGTH;

        // Create the new translation for the pose
        Translation3d newTranslation = initialPose.getTranslation()
                .plus(new Translation3d(displacementX, displacementY, displacementZ));

        // Return a new Pose3d with the updated translation (same rotation)
        Pose3d newPose = new Pose3d(newTranslation, initialPose.getRotation());
        // Output the new pose
        System.out.println("New Pose: " + newPose);
    }

}

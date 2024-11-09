package frc.robot;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;

public class Transform {
        public static Pose3d getPose10cmForward(Pose3d currentPose) {
                // Get the rotation of the current pose
                Rotation3d rotation = currentPose.getRotation();
                
                // Extract yaw, pitch, and roll from the Rotation3d
                double yaw = rotation.getZ();  // Yaw (rotation around Z-axis)
                double pitch = rotation.getY();  // Pitch (rotation around Y-axis)
                double roll = rotation.getX();  // Roll (rotation around X-axis)
                
                // Calculate the direction vector using all three components
                double forwardX = Math.cos(yaw) * Math.cos(pitch);  // Combine yaw and pitch for X direction
                double forwardY = Math.sin(yaw) * Math.cos(pitch);  // Combine yaw and pitch for Y direction
                double forwardZ = Math.sin(pitch);  // Use pitch for Z direction
        
                // Create a vector that is 10 cm (0.1 meters) in the direction of the rotation
                double displacementX = forwardX * 0.1;  // 10 cm forward along X
                double displacementY = forwardY * 0.1;  // 10 cm forward along Y
                double displacementZ = forwardZ * 0.1;  // 10 cm forward along Z (vertical)
        
                // Create the new translation for the pose
                Translation3d newTranslation = currentPose.getTranslation()
                                                          .plus(new Translation3d(displacementX, displacementY, displacementZ));
        
                // Return a new Pose3d with the updated translation (same rotation)
                return new Pose3d(newTranslation, currentPose.getRotation());
            }
        
            public static void main(String[] args) {
                // Example Pose3d, assuming it has some rotation (e.g., facing 45 degrees in the XY-plane and slightly pitched)
                Pose3d currentPose = new Pose3d(0, 0, 0, new Rotation3d(Math.toRadians(0), Math.toRadians(45), Math.toRadians(0)));  // Some rotation
                
                // Get a new Pose3d 10 cm forward
                Pose3d newPose = getPose10cmForward(currentPose);
                
                // Output the new pose
                System.out.println("New Pose: " + newPose);
            }

}

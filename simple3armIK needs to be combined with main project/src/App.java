import java.util.Scanner;

public class App {
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);

		double arm1Length = 15;
		double arm2Length = 10;
		double arm3Length = 10;

		// System.out.println("Enter endefector x: ");
		// double x = scanner.nextDouble();
		// System.out.println("Enter endefector y: ");
		// double y = scanner.nextDouble();
		// System.out.println("Enter endefector angle: ");
		// double angle = scanner.nextDouble();
		// scanner.close();

		double x = 18.5;
		double y = 15;
		double angle = 330;

		Pose2D endPose2d = new Pose2D(x, y);

		Vector2D arm3 = Vector2D.toCartesian(arm3Length, Math.toRadians(angle));

		Vector2D baseToArm2Vector = Vector2D.subtract(new Vector2D(endPose2d.getX(), endPose2d.getY()), arm3);

		double arm1TriangleAngle = Math.toDegrees(Math
				.acos((arm1Length * arm1Length + baseToArm2Vector.getLengthSq() - arm2Length * arm2Length)
						/ (2 * arm1Length * baseToArm2Vector.getLength())));

		double arm2TriangleAngle = Math.toDegrees(Math.asin(
				(baseToArm2Vector.getLength() * Math.sin(Math.toRadians(arm1TriangleAngle)))
						/ arm2Length));



		double arm1Angle = wrapAngle(arm1TriangleAngle + Math.toDegrees(baseToArm2Vector.getAngle()));

		double arm2Angle = wrapAngle(90 - arm2TriangleAngle);

		double arm3Angle = wrapAngle(angle - (180 - (arm2TriangleAngle - arm1Angle)) - 90);

		System.out.println("Arm 1 angle: " + arm1Angle);
		System.out.println("Arm 2 angle: " + arm2Angle);
		System.out.println("Arm 3 angle: " + arm3Angle);

	}

	public static double wrapAngle(double angle) {
		double result = angle % 360.0;
		if (result < 0) {
			result += 360;
		}
		return result;
	}

}

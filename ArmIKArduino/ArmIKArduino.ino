#include "Pose2D.h"
#include "Vector2D.h"
#include <Adafruit_PWMServoDriver.h> 

// Create the PWM driver object
Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver();

// Servo configuration
#define SERVOMIN  150  // Minimum pulse length count (0 degrees)
#define SERVOMAX  600  // Maximum pulse length count (180 degrees)
#define SERVOFREQ 60   // Analog servos run at ~60 Hz updates

const double arm1Length = 15;
const double arm2Length = 10;
const double arm3Length = 10;

const int arm1Port = 1;
const int arm2Port = 2;
const int arm3Port = 8;

const double arm1Offset = 0;
const double arm2Offset = -15;
const double arm3Offset = -15;


double x = 0, y = 0, angle = 0;

void setup() {
    Serial.begin(9600);

    pwm.begin();
    pwm.setPWMFreq(SERVOFREQ);  // Set the PWM frequency to 60 Hz
    delay(10);
}

// Function to wrap angle between 0 and 360 degrees
float wrapAngle(float angle) {
  float result = fmod(angle, 360.0);
  if (result < 0) {
    result += 360.0;
  }
  return result;
}

void flushSerial() {
    while (Serial.available() > 0) {
        Serial.read(); // Clear the input buffer
    }
}

void loop() {
    if (Serial.available() > 0) {
        // Read end effector x
        Serial.print("Enter end effector x: ");
        while (Serial.available() == 0) {} // Wait for input
        x = Serial.parseFloat(); // Use parseFloat for double
        flushSerial(); // Clear the buffer before the next input
        Serial.println(x); // Print the input for confirmation

        // Read end effector y
        Serial.print("Enter end effector y: ");
        while (Serial.available() == 0) {} // Wait for input
        y = Serial.parseFloat(); // Use parseFloat for double
        flushSerial(); // Clear the buffer before the next input
        Serial.println(y); // Print the input for confirmation

        // Read end effector angle
        Serial.print("Enter end effector angle: ");
        while (Serial.available() == 0) {} // Wait for input
        angle = Serial.parseFloat(); // Use parseFloat for double
        flushSerial(); // Clear the buffer after the last input
        Serial.println(angle); // Print the input for confirmation

        // Create Pose2D and Vector2D instances
        Pose2D endPose2d(x, y);
        Vector2D arm3Vector = Vector2D::toCartesian(arm3Length, radians(angle));

        // Calculate base to arm 2 vector and arm angles
        Vector2D baseToArm2Vector(endPose2d.getX(), endPose2d.getY());

        baseToArm2Vector.subtract(arm3Vector);

        double arm1TriangleAngle = degrees(
            acos((arm1Length * arm1Length + baseToArm2Vector.getLengthSq() - arm2Length * arm2Length)
                / (2 * arm1Length * baseToArm2Vector.getLength())));

        if(isnan(arm1TriangleAngle)){
          Serial.println("pose is impossible for the arm");
          return;
        }

        double arm2TriangleAngle = degrees(
          asin((baseToArm2Vector.getLength() * sin(radians(arm1TriangleAngle)))
                  / arm2Length));

        double arm1Angle = wrapAngle(arm1TriangleAngle + degrees(baseToArm2Vector.getAngle())) + arm1Offset;
        
		    double arm2Angle = wrapAngle(90 - arm2TriangleAngle) + arm2Offset;

        double arm3Angle = wrapAngle(angle - (180 - (arm2TriangleAngle - arm1Angle)) - 90) + arm3Offset;

        arm1Angle = 180 - arm1Angle;


        setServoPose(String(arm1Port) + " " + String(arm1Angle) + " " +
                     String(arm2Port) + " " + String(arm2Angle) + " " +
                     String(arm3Port) + " " + String(arm3Angle));


        // Output the calculated arm angles
        Serial.print("Arm 1 angle: ");
        Serial.println(arm1Angle);
        Serial.print("Arm 2 angle: ");
        Serial.println(arm2Angle);
        Serial.print("Arm 3 angle: ");
        Serial.println(arm3Angle);
    }
}


// Function to set servo angles based on input string
void setServoPose(String pose) {
  int servo, angle;
  char* part;
  char input[64];
  
  // Convert String to char array
  pose.toCharArray(input, 64);
  
  part = strtok(input, " ");  // Split input by space
  while (part != NULL) {
    servo = atoi(part);      // Convert to servo index
    part = strtok(NULL, " ");
    if (part != NULL) {
      angle = atoi(part);    // Convert to angle
      setServoAngle(servo, angle);
      part = strtok(NULL, " ");
    }
  }
}

// Function to set the angle of a single servo
void setServoAngle(uint8_t servo, uint16_t angle) {
  // Constrain the angle to be between 0 and 180 degrees
  angle = constrain(angle, 0, 180);

  // Map the angle to the pulse length
  uint16_t pulseLength = map(angle, 0, 180, SERVOMIN, SERVOMAX);

  // Set the servo to the desired angle
  pwm.setPWM(servo, 0, pulseLength);

  Serial.print("Servo ");
  Serial.print(servo);
  Serial.print(" set to ");
  Serial.print(angle);
  Serial.println(" degrees.");
}


#include <Wire.h>
#include <Adafruit_PWMServoDriver.h>

// Create the PWM driver object
Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver();

// Servo configuration
#define SERVOMIN  150  // Minimum pulse length count (0 degrees)
#define SERVOMAX  600  // Maximum pulse length count (180 degrees)
#define SERVOFREQ 60   // Analog servos run at ~60 Hz updates

void setup() {
  Serial.begin(9600);
  Serial.println("Initializing...");

  pwm.begin();
  pwm.setPWMFreq(SERVOFREQ);  // Set the PWM frequency to 60 Hz
  delay(10);
  
  // Example: Set initial poses
  // setServoPose("0 45 1 90");  // Set servo 0 to 45 degrees and servo 1 to 90 degrees
  delay(1000);                // Wait for 1 second
}

void loop() {
  // Listen for serial input to set servo poses
  if (Serial.available()) {
    String input = Serial.readStringUntil('\n');  // Read user input
    setServoPose(input);  // Move all servos to specified angles
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

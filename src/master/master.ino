#include <Smartcar.h>

const unsigned short LEFT_ODOMETER_PIN = 2;
const unsigned short RIGHT_ODOMETER_PIN = 3;

BrushedMotor leftMotor(8, 10, 9);
BrushedMotor rightMotor(12, 13, 11);
DifferentialControl control(leftMotor, rightMotor);

const int TRIGGER_PIN = 40; 
const int ECHO_PIN = 41; 
const unsigned int MAX_DISTANCE = 50;

SR04 front(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

GY50 gyroscope(37);
DirectionlessOdometer leftOdometer(100);
DirectionlessOdometer rightOdometer(100);

SmartCar car(control, gyroscope, leftOdometer, rightOdometer);

void setup() {
  Serial3.begin(9600);
  Serial.begin(9600);
}

void loop() {
  handleInput();
}

void handleInput() { //handle serial input if there is any
  if (Serial3.available()) {
    char input = Serial3.read(); //read everything that has been received so far and log down the last entry
    switch (input) {
      case 'a': //manual control mode
        manualControl();
        break;
      case 'b': //automated parking
        automatedParking();
        break;
      case 'c': //line following
        lineFollowing();
        break;
      case 't': //make a triangle
        triangle();
        break; 
      case 'd': //make a triangle
        drive();
        break;   
      default: 
        Serial3.println("Unknown command");
    }
  }
}

void manualControl(){

  int fSpeed = 70; //70% of the full speed forward
  int bSpeed = -70; //70% of the full speed backward
  int lDegrees = -75; //degrees to turn left
  int rDegrees = 75; //degrees to turn right
  
  while (Serial3.available()){
  char input = Serial3.read(); //read everything that has been received so far and log down the last entry
    switch (input) {
      case '2': 
        fSpeed = 20;
        bSpeed = 20; 
        Serial3.println("20");
        break;
      case '4': 
        fSpeed = 40;
        bSpeed = 40;
        Serial3.println("40"); 
        break;
      case '6': 
        fSpeed = 60;
        bSpeed = 60; 
        Serial3.println("60");
        break;
      case '8': 
        fSpeed = 80;
        bSpeed = 80; 
        Serial3.println("80");
        break;
      case '0': 
        fSpeed = 100;
        bSpeed = 100; 
        Serial3.println("100");
        break;  
      case 'l': //rotate counter-clockwise going forward
        car.setSpeed(fSpeed);
        car.setAngle(lDegrees);
        break;
      case 'r': //turn clock-wise
        car.setSpeed(fSpeed);
        car.setAngle(rDegrees);
        break;
      case 'f': //go ahead
        car.setSpeed(fSpeed);
        car.setAngle(0);
        break;
      case 'b': //go back
        car.setSpeed(bSpeed);
        car.setAngle(0);
        break;
      case 's': //stop
        car.setSpeed(0);
        car.setAngle(0);
        break;
      case 'q': 
        car.setSpeed(0);
        car.setAngle(0);
        loop();
        break;  
      default: //if you receive something that you don't know, just stop
        car.setSpeed(0);
        car.setAngle(0);
    }
  }
  
  }

void triangle(){

  const float carSpeed = 1.0;
  const long distanceToTravel = 40;
  const int degreesToTurn = 120;
  
  leftOdometer.attach(LEFT_ODOMETER_PIN, []() {
    leftOdometer.update();
  });
  rightOdometer.attach(RIGHT_ODOMETER_PIN, []() {
    rightOdometer.update();
  });

  car.enableCruiseControl();

  // Travel around an imaginary square
  go(distanceToTravel, carSpeed);
  rotate(degreesToTurn, carSpeed);

  go(distanceToTravel, carSpeed);
  rotate(degreesToTurn, carSpeed);

  go(distanceToTravel, carSpeed);
  rotate(degreesToTurn, carSpeed);

  car.disableCruiseControl();

  loop();

  
  }

void rotate(int degrees, float speed) {
  speed = smartcarlib::utils::getAbsolute(speed);
  degrees %= 360; // Put degrees in a (-360,360) scale
  if (degrees == 0) {
    return;
  }

  car.setSpeed(speed);
  if (degrees > 0) {
    car.setAngle(90);
  } else {
    car.setAngle(-90);
  }

  unsigned int initialHeading = car.getHeading();
  bool hasReachedTargetDegrees = false;
  while (!hasReachedTargetDegrees) {
    car.update();
    int currentHeading = car.getHeading();
    if (degrees < 0 && currentHeading > initialHeading) {
      // If we are turning left and the current heading is larger than the
      // initial one (e.g. started at 10 degrees and now we are at 350), we need to substract 360
      // so to eventually get a signed displacement from the initial heading (-20)
      currentHeading -= 360;
    } else if (degrees > 0 && currentHeading < initialHeading) {
      // If we are turning right and the heading is smaller than the
      // initial one (e.g. started at 350 degrees and now we are at 20), so to get a signed displacement (+30)
      currentHeading += 360;
    }
    // Degrees turned so far is initial heading minus current (initial heading
    // is at least 0 and at most 360. To handle the "edge" cases we substracted or added 360 to currentHeading)
    int degreesTurnedSoFar = initialHeading - currentHeading;
    hasReachedTargetDegrees = smartcarlib::utils::getAbsolute(degreesTurnedSoFar) >= smartcarlib::utils::getAbsolute(degrees);
  }

  car.setSpeed(0);
}

/**
   Makes the car travel at the specified distance with a certain speed
   @param centimeters   How far to travel in centimeters, positive for
                        forward and negative values for backward
   @param speed         The speed to travel
*/
void go(long centimeters, float speed) {
  if (centimeters == 0) {
    return;
  }
  // Ensure the speed is towards the correct direction
  speed = smartcarlib::utils::getAbsolute(speed) * ((centimeters < 0) ? -1 : 1);
  car.setAngle(0);
  car.setSpeed(speed);

  long initialDistance = car.getDistance();
  bool hasReachedTargetDistance = false;
  while (!hasReachedTargetDistance) {
    car.update();
    auto currentDistance = car.getDistance();
    auto travelledDistance = initialDistance > currentDistance ? initialDistance - currentDistance : currentDistance - initialDistance;
    hasReachedTargetDistance = travelledDistance >= smartcarlib::utils::getAbsolute(centimeters);
  }
  car.setSpeed(0);
}

void drive(){
  
  char input;

  car.enableCruiseControl();

  while (Serial3.available() && input != 'q'){
    
  input = Serial3.read();
    
  car.update();
  car.setSpeed(0.5);

  if (front.getDistance() < 15 && front.getDistance() > 0) {
    rotate(90, 0.5);
  
  }

  if (front.getDistance() < 15 && front.getDistance() > 0) {
    rotate(180, 0.5);
  
  }

  }

  car.disableCruiseControl();
}
void automatedParking(){}

void lineFollowing(){}

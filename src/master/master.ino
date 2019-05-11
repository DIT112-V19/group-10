#include <Smartcar.h>

const unsigned short LEFT_ODOMETER_PIN = 2;
const unsigned short RIGHT_ODOMETER_PIN = 3;

BrushedMotor leftMotor(8, 10, 9);
BrushedMotor rightMotor(12, 13, 11);
DifferentialControl control(leftMotor, rightMotor);

GY50 gyroscope(37);
DirectionlessOdometer leftOdometer(100);
DirectionlessOdometer rightOdometer(100);

SmartCar car(control, gyroscope, leftOdometer, rightOdometer);

void setup() {
  Serial3.begin(9600);
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
      default: 
        Serial3.println("Unknown command");
    }
  }
}

void manualControl(){

  const int fSpeed = 70; //70% of the full speed forward
  const int bSpeed = -70; //70% of the full speed backward
  const int lDegrees = -75; //degrees to turn left
  const int rDegrees = 75; //degrees to turn right
  
  char input = Serial3.read(); //read everything that has been received so far and log down the last entry
    switch (input) {
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

void automatedParking(){}

void lineFollowing(){}
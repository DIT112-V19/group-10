#include <Smartcar.h>

#define trigPin 10
#define echoPin 9

const unsigned short LEFT_ODOMETER_PIN = 2;
const unsigned short RIGHT_ODOMETER_PIN = 3;

BrushedMotor leftMotor(8, 10, 9); // from premade example automatedmovement for reference. 
BrushedMotor rightMotor(12, 13, 11);
DifferentialControl control(leftMotor, rightMotor);

GY50 gyroscope(37);
DirectionlessOdometer leftOdometer(100);
DirectionlessOdometer rightOdometer(100);

SmartCar car(control, gyroscope, leftOdometer, rightOdometer);

void setup() {  
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT); 
  
  leftOdometer.attach(LEFT_ODOMETER_PIN, []() {
    leftOdometer.update();
  });
  
  rightOdometer.attach(RIGHT_ODOMETER_PIN, []() {
    rightOdometer.update();
  });
  
  car.enableCruiseControl();
  car.setSpeed(2.0);
}

void loop() {
  
  long distance; 
  long duration;
 
  digitalWrite(trigPin, LOW);  
  delayMicroseconds(2); 
  
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10); 
  
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);
  
  distance = duration / 58.2; // here we determine the distance in cm

 // if(distance < 15) {
    //Tarik here you will fill in parameters
//}

}
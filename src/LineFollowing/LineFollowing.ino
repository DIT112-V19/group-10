// connect the sensors to digital pins
#include <Smartcar.h>

#define LEFT_SENSORPIN 34
#define CENTER_SENSORPIN 35
#define RIGHT_SENSORPIN 33

const unsigned short LEFT_ODOMETER_PIN = 2;
const unsigned short RIGHT_ODOMETER_PIN = 3;

int frontMotorLeft = 8;
int rearMotorLeft = 10;
int speedPinLeft = 9;
int frontMotorRight = 12;
int rearMotorRight = 13;
int speedPinRight = 11;

BrushedMotor leftMotor(frontMotorLeft, rearMotorLeft, speedPinLeft);
BrushedMotor rightMotor(frontMotorRight, rearMotorRight, speedPinRight);
const unsigned int MAX_DISTANCE = 50;

DifferentialControl control(leftMotor, rightMotor);

GY50 gyroscope(37);
DirectionlessOdometer leftOdometer(100);
DirectionlessOdometer rightOdometer(100);

SmartCar car(control, gyroscope, leftOdometer, rightOdometer);


void setup()
{
 Serial.begin(9600);
 pinMode(LEFT_SENSORPIN,INPUT);
 pinMode(CENTER_SENSORPIN,INPUT);
 pinMode(RIGHT_SENSORPIN,INPUT);
 
   leftOdometer.attach(LEFT_ODOMETER_PIN, []() {
    leftOdometer.update();
  });
  
  rightOdometer.attach(RIGHT_ODOMETER_PIN, []() {
    rightOdometer.update();
  });

  car.enableCruiseControl();
 car.setSpeed(0.2);
}

void loop()
{
   car.update();

 // read sensor Inputs
 byte leftSensor=digitalRead(LEFT_SENSORPIN);
 byte centerSensor=digitalRead(CENTER_SENSORPIN);
 byte rightSensor=digitalRead(RIGHT_SENSORPIN);

//Case Forward
 if(leftSensor ==1  && rightSensor == 1 && centerSensor==0)
 {
  digitalWrite(frontMotorRight, HIGH);
  digitalWrite(rearMotorRight, LOW);
  digitalWrite(frontMotorLeft, HIGH);
  digitalWrite(rearMotorLeft, LOW);
  delay(50);
}
  
 //Case Left
 else if(leftSensor ==1  && rightSensor == 0 && centerSensor==1)
 {
  digitalWrite(frontMotorLeft, HIGH);
  digitalWrite(rearMotorLeft, LOW);
  digitalWrite(frontMotorRight, LOW);
  digitalWrite(rearMotorRight, LOW);

  delay(50);
 }
  //Case Right
 else if(leftSensor ==0  && rightSensor == 1 && centerSensor==1)
 {
  digitalWrite(frontMotorRight, HIGH);
  digitalWrite(rearMotorRight, LOW);
  digitalWrite(frontMotorLeft, LOW);
  digitalWrite(rearMotorLeft, LOW);
  delay(50);
 }
 }

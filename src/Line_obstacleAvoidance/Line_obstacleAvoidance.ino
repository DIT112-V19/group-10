#include <Smartcar.h>

//ir sensor
#define LEFT_SENSORPIN 6
#define CENTER_SENSORPIN 7
#define RIGHT_SENSORPIN 4



//Motor
int frontMotorLeft = 8;
int rearMotorLeft = 10;
int speedPinLeft = 9;
int frontMotorRight = 12;
int rearMotorRight = 13;
int speedPinRight = 11;


//Obstacle Avoidance
const int TRIGGER_PIN = 47; 
const int ECHO_PIN = 49;

const unsigned short LEFT_ODOMETER_PIN = 2;
const unsigned short RIGHT_ODOMETER_PIN = 3;
const unsigned int MAX_DISTANCE = 100;
const int STOPPING_DISTANCE= 15;

SR04 front(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

BrushedMotor leftMotor(frontMotorLeft, rearMotorLeft, speedPinLeft);
BrushedMotor rightMotor(frontMotorRight, rearMotorRight, speedPinRight);

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
 car.setSpeed(0.4);
 }


 void loop()
 {
   car.update();

  Serial.println(front.getDistance());

  // read sensor Inputs
  byte leftSensor=digitalRead(LEFT_SENSORPIN);
  byte centerSensor=digitalRead(CENTER_SENSORPIN);
  byte rightSensor=digitalRead(RIGHT_SENSORPIN);
  int distance=front.getDistance();
if( (distance < STOPPING_DISTANCE) && (distance > 0) ){
  Serial.print("Here we go");
    Serial.print(distance);

  pause();
  delay(950);
  right();
  delay(1000);
  forward();
  delay(1000);
  pause();
  delay(1000);
  left();
  delay(1500);
  forward();
  delay(1400);
  right();
  delay(500);
 }else{
  
  
/*LineFollowing cases starts here
 __________________________________________________________________
 Case Forward*/
if(leftSensor ==1  && rightSensor == 1 && centerSensor==0)
 {
  forward();
  delay(50);
 }
  
 //Case Left
 else if(leftSensor ==1  && rightSensor == 0 && centerSensor==1)
 {
  left();
  delay(75);
 }
 
  else if(leftSensor ==1  && rightSensor == 0 && centerSensor==0)
 {
  left();
  delay(75);
 }
  //Case Right
 else if(leftSensor ==0  && rightSensor == 1 && centerSensor==1)
 {
  right();
  delay(75);
 }
 
  else if(leftSensor ==0  && rightSensor == 1 && centerSensor==1)
 {
  right ();
  delay(75);
 }
 
 if(leftSensor ==1  && rightSensor == 1 && centerSensor==1)
 {
  pause();
  delay(50);
 }
 }
 }
 
 

 void pause(){
  digitalWrite(frontMotorRight, LOW);
  digitalWrite(rearMotorRight, LOW);
  digitalWrite(frontMotorLeft, LOW);
  digitalWrite(rearMotorLeft, LOW);
  Serial.print("P");

 }

 
void left(){
  digitalWrite(frontMotorLeft, HIGH);
  digitalWrite(rearMotorLeft, LOW);
  digitalWrite(frontMotorRight, LOW);
  digitalWrite(rearMotorRight, LOW);
  Serial.print("L");
 }

 
 void right(){
  digitalWrite(frontMotorRight, HIGH);
  digitalWrite(rearMotorRight, LOW);
  digitalWrite(frontMotorLeft, LOW);
  digitalWrite(rearMotorLeft, LOW);
  Serial.print("R");

 }

 void forward(){
  digitalWrite(frontMotorRight, HIGH);
  digitalWrite(rearMotorRight, LOW);
  digitalWrite(frontMotorLeft, HIGH);
  digitalWrite(rearMotorLeft, LOW);
  Serial.print("F");

 }
 

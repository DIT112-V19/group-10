#include <Smartcar.h>

#define LEFT_SENSORPIN 6
#define CENTER_SENSORPIN 7
#define RIGHT_SENSORPIN 4

int frontMotorLeft = 8;
int rearMotorLeft = 10;
int speedPinLeft = 9;
int frontMotorRight = 12;
int rearMotorRight = 13;
int speedPinRight = 11;

const int STOPPING_DISTANCE= 15;

const unsigned short LEFT_ODOMETER_PIN = 2;
const unsigned short RIGHT_ODOMETER_PIN = 3;

int fSpeed = 70;    //70% of the full speed forward
int bSpeed = -70;   //70% of the full speed backward
int lDegrees = -75; //degrees to turn left
int rDegrees = 75;  //degrees to turn right

BrushedMotor leftMotor(frontMotorLeft, rearMotorLeft, speedPinLeft);
BrushedMotor rightMotor(frontMotorRight, rearMotorRight, speedPinRight);
DifferentialControl control(leftMotor, rightMotor);

const int TRIGGER_PIN = 47;
const int ECHO_PIN = 49;
const unsigned int MAX_DISTANCE = 50;

//Ultrasonic sensor
SR04 front(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

//Gyroscope
GY50 gyroscope(21);
DirectionlessOdometer leftOdometer(100);
DirectionlessOdometer rightOdometer(100);

//Arduino Car
SmartCar car(control, gyroscope, leftOdometer, rightOdometer);

void setup()
{
  //Serial3 is used for the bluetooth
  Serial3.begin(9600);
  Serial.begin(9600);
}

void loop()
{
  handleInput();
}



//Serves as our menu
void handleInput() {

  //handle serial input if there is any
  if (Serial3.available()) {
    //it reads everything that has been received so far and log down the last entry
    char input = Serial3.read();

    //menu options using a switchCase
    switch (input) {

      case 'a': //manual control mode
        manualControl();
        break;
      
      case 'b': //autonomous control mode
        autonomousMode();
        break;
      
      default:
        Serial3.println("Unknown command");
        break;
    }
  }
}

//Switchase for different options of autonomousMode
void autonomousMode() {
  char input;
  
  do {
    if (Serial3.available()) {
      input = Serial3.read();
      switch (input) {
        
        case 'c': // line following 
          lineFollowing();
          break;
        
        case 't': // Drive one full time in triangle without stop
          triangle();
          break;
        
        case 'd': // drive autonomously without hitting an obstacle
          drive();
          break;
        
        case 's': //Stops the car
          car.setSpeed(0);
          car.setAngle(0);
          break;
        
        case 'q': //Stops the car then returns to menu
          car.setSpeed(0);
          car.setAngle(0);
          handleInput();
          break;

        default: // if you receive something that you don't know, just stop
          car.setSpeed(0);
          car.setAngle(0);
          break;
      }
    }
  } while (input != 'q'); //remain in this menu as long as option is not 'q'
}

//Enter menu for manually controlling the car
void manualControl() {
  char input;
  
  do {
    if (Serial3.available()) {
      input = Serial3.read();
    
      switch (input) {
      //these cases are for setting the speed of the car when altering the speedbar in manual mode
        
        case '2': // 20% speed
          fSpeed = 20;
          bSpeed = -20;
          break;
      
        case '4': // 40% speed
          fSpeed = 40;
          bSpeed = -40;
          break;
      
        case '6': // 60% speed
          fSpeed = 60;
          bSpeed = -60;
          break;
      
        case '8': // 80% speed
          fSpeed = 80;
          bSpeed = -80;
          break;
      
        case '0': // 100% speed
          fSpeed = 100;
          bSpeed = -100;
          break;
      
      //These cases are for the direction-arrows and stop-button of the manual mode
        
        case 'x': // stop car
          car.setSpeed(0);
          car.setAngle(0);
          break;

        case 'l': //rotate counter-clockwise going forward
          Serial.println(input);
          car.setSpeed(fSpeed);
          car.setAngle(lDegrees);
          break;

        case 'r': //turn clock-wise
          Serial.println(input);
          car.setSpeed(fSpeed);
          car.setAngle(rDegrees);
          break;

        case 'f': //go forward
          Serial.println(input);
          car.setSpeed(fSpeed);
          car.setAngle(0);
          break;

        case 'b': //go backward
          Serial.println(input);
          car.setSpeed(bSpeed);
          car.setAngle(0);
          break;

        case 'q': //return to main menu
          Serial.println(input);
          car.setSpeed(0);
          car.setAngle(0);
          handleInput();
          break;

        default: //if you receive something that you don't know, just stop
          car.setSpeed(0);
          car.setAngle(0);
          break;
      }
    }
  } while (input != 'q');  //Remain in these cases until we want to return to main menu
}
  
//Move in a triangle
void triangle(){
  char input = 't';
 
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

  // Travel around an imaginary triangle
  go(distanceToTravel, carSpeed);
  rotate(degreesToTurn, carSpeed);

  go(distanceToTravel, carSpeed);
  rotate(degreesToTurn, carSpeed);

  go(distanceToTravel, carSpeed);
  rotate(degreesToTurn, carSpeed);
  
  delay(1000);
  car.disableCruiseControl();
}

//Method to rotate in triangle
void rotate(int degrees, float speed){
  speed = smartcarlib::utils::getAbsolute(speed);
  degrees %= 360; // Put degrees in a (-360,360) scale
  if (degrees == 0){
    return;
  }

  car.setSpeed(speed);
  if (degrees > 0){
    car.setAngle(90);
  } else {
    car.setAngle(-90);
  }

  unsigned int initialHeading = car.getHeading();
  bool hasReachedTargetDegrees = false;
  
  while (!hasReachedTargetDegrees){
    car.update();
    int currentHeading = car.getHeading();

    if (degrees < 0 && currentHeading > initialHeading){
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

//Method for autonomous driving with obstacle avoidance
void drive() {
  char input;
  input='d';
  do{

    if (true) {  
      input = Serial3.read();
      car.overrideMotorSpeed(40, 40);
      Serial.println(front.getDistance());

      if ((front.getDistance() < 15) && (front.getDistance() > 0)) {
        car.overrideMotorSpeed(-50, 50);
        delay(500);
      } 
      Serial3.println(car.getSpeed());
    }

  } while(input!='s');
    car.overrideMotorSpeed(0, 0);
}
  
//code for linefollowing with integrated obstacle avoidance
void lineFollowing() {
  char input = 'c';

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
  
  while (input != 's') {
    
    if (true) {
      car.update();
      Serial.println(front.getDistance());

      // read sensor Inputs
      byte leftSensor=digitalRead(LEFT_SENSORPIN);
      byte centerSensor=digitalRead(CENTER_SENSORPIN);
      byte rightSensor=digitalRead(RIGHT_SENSORPIN);
      
      int distance=front.getDistance();

      //Condition for obstacle avoidance while following the line 
      if( (distance < STOPPING_DISTANCE) && (distance > 0) ){
       
        Serial.print("Entering obstacleAvoidance");
        Serial.print(distance);
        
        //Stop infront of object
        pause();
        delay(850);
        
        //turn right
        right();
        delay(900);
        
        //go forward
        forward();
        delay(900);
        
        //Stop
        pause();
        delay(1000);
        
        //Turn back towards line
        left();
        delay(1600);
        
        //Go towards line
        forward();
        delay(1300);
        
        //Straighten up the car towards the line
        right();
        delay(500);
 
      } else {
        
        //LineFollowing IR sensor conditions begins here
        
        //Case Forward
        if(leftSensor ==1  && rightSensor == 1 && centerSensor==0) {
          forward();
          delay(50);
        }
          
        //Case Left if two sensors reads
        else if(leftSensor ==1  && rightSensor == 0 && centerSensor==1) {
          left();
          delay(75);
        }
        
        //Case left if one sensor reads
        else if(leftSensor ==1  && rightSensor == 0 && centerSensor==0) {
          left();
          delay(75);
        }
        
        //Case Right if two sensor reads
        else if(leftSensor ==0  && rightSensor == 1 && centerSensor==1) {
          right();
          delay(75);
        }
        
        //Case Right if one sensor reads
        else if(leftSensor ==0  && rightSensor == 1 && centerSensor==1) {
          right ();
          delay(75);
        }
        
        //Case stop at end of line
        if(leftSensor ==1  && rightSensor == 1 && centerSensor==1) {
          pause();
          delay(50);
        }
      }
    }
    input = Serial3.read();
  }
  //if input is 's' which means stop the car, we exit cruisecontrol and stop the car.
  car.disableCruiseControl();
  car.setSpeed(0);
  car.setAngle(0);
}

//Stop method for the car
void pause() {
  digitalWrite(frontMotorRight, LOW);
  digitalWrite(rearMotorRight, LOW);

  digitalWrite(frontMotorLeft, LOW);
  digitalWrite(rearMotorLeft, LOW);
  Serial.print("P");
}

//Method to turn left 
void left() {
  digitalWrite(frontMotorLeft, HIGH);
  digitalWrite(rearMotorLeft, LOW);
  
  digitalWrite(frontMotorRight, LOW);
  digitalWrite(rearMotorRight, LOW);
  Serial.print("L");
}

 //Method to turn right
 void right(){
  digitalWrite(frontMotorRight, HIGH);
  digitalWrite(rearMotorRight, LOW);
  
  digitalWrite(frontMotorLeft, LOW);
  digitalWrite(rearMotorLeft, LOW);
  Serial.print("R");
}

//Method to go forward
 void forward(){
  digitalWrite(frontMotorRight, HIGH);
  digitalWrite(rearMotorRight, LOW);
  
  digitalWrite(frontMotorLeft, HIGH);
  digitalWrite(rearMotorLeft, LOW);
  Serial.print("F");
}

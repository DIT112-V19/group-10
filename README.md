# Sally Carrera  [![Build Status](https://travis-ci.org/DIT112-V19/group-10.svg?branch=master)](https://travis-ci.org/DIT112-V19/group-10) [![codecov](https://codecov.io/gh/DIT112-V19/group-10/branch/master/graph/badge.svg)](https://codecov.io/gh/DIT112-V19/group-10/branch/master) ![Image description](https://vignette.wikia.nocookie.net/pixar/images/4/4b/Sally_carrera.png/revision/latest?cb=20140314011950)

## Project Proposal
Self driving cars are getting more and more popular as we develop and refine the technology needed. We believe that in the near future this new technology will be part of our daily lives and can be seen more and more driving in our streets.

### WHAT ? 
Our product aims to simulate a real-world driving experience and introduces the user to some aspects of self driving cars.

### WHY ?
We believe that such experience will help reducing the fears and distrust surrounding the current advancements. We want this technology to be accessible as we believe that anyone should be able to experience the benefits of a self driving car and an insight into the technology behind it.

### HOW ?
This product works on two platforms: an Arduino car and a mobile application.
The car will be able to drive autonomously by following a line and avoiding obstacles. Using a mobile app, the user will be able to manually control the car. Both the car and the app will have security measures, to prevent unauthorized access to the car. The app will allow the user to easily switch between autonomous and manual mode.


### FEATURES
##### Obstacle avoidance
This ensures the car does not crash when in autonomous mode. Using the ultrasonic sensor, the car will always be a reasonable distance away from any obstacle. Once an obstacle has been detected, the car will drive around it and return to its course. It will be a challenge to ensure the car reacts quickly to an object in its path.
##### Line detection
This feature allows the car to drive itself around a given course, staying on a line. Using an infrared sensor, the car will always remain on the track. We anticipate a challenge with ensuring the car moves as expected around turns and bends.
##### Speed control
The app will give the user the opportunity to control the speed of the car, using pre-built controls. The range will be from zero to maximum speed. This gives the user more control over the car and is available across both modes. The challenge here would be to make sure that the input from the user is able to quickly affect the speed of the car.
##### Steering
This feature gives the user manual control over the car. Using the app, the user can steer the car by adjusting the speed of the wheels on either side to turn. It will be challenging to ensure the accuracy of the angle the car is turning so that it turns as expected.
##### Code quality
We emphasize high code quality in our work: we take a step beyond functionality and invest in maintainability. Master branch is protected by continuous integration platform that builds and tests the code when any change is applied. Additionally, unit test coverage report is issued. Mobile app coverage is continuously increasing. Arduino code tests are planned.


### HARDWARE ARCHITECTURE

 1. ST-4WD Smart Car Chassis 4WD Speed Car Magnetic Motor Single Layer For Arduino
 2. Ultasonic Sensor HC-SR04 (1)
 3. Arduino Mega 2560 REV3
 4. SmartCar Shield
 4. HC-O6 Bluetooth Module
 5. Funduino 3 Ch Infrared White/Black Line Detection Module MD-159
 6. AA Batteries (8)
 7. Odometer (2)
 8. Pin Jumpers
 9. Gyroscopes GY-50 L3G4200
 
 
 ### SOFTWARE ARCHITECTURE
 The high-level overview of our two-tier system is depicted in the picture below:
![deployment diagram](https://github.com/DIT112-V19/group-10/blob/documentation/DeploymentDiagram.png)
 ....
 
 ### HOW TO SET UP 
 ...
 ### DEPENDENCIES
 ...
 
### REFERENCE / DOCUMENTATION



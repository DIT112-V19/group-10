# Sally Carrera  [![Build Status](https://travis-ci.org/DIT112-V19/group-10.svg?branch=master)](https://travis-ci.org/DIT112-V19/group-10) [![codecov](https://codecov.io/gh/DIT112-V19/group-10/branch/master/graph/badge.svg)](https://codecov.io/gh/DIT112-V19/group-10/branch/master) ![Image description](https://vignette.wikia.nocookie.net/pixar/images/4/4b/Sally_carrera.png/revision/latest?cb=20140314011950)

## Project Proposal
Self driving cars are getting more and more popular as we develop and refine the technology needed. We believe that in the near future this new technology will be part of our daily lives and can be seen more and more driving in our streets.

### WHAT? 
Our product aims to simulate a real-world driving experience and introduces the user to some aspects of self driving cars.

### WHY?
We believe that such experience will help reducing the fears and distrust surrounding the current advancements. We want this technology to be accessible as we believe that anyone should be able to experience the benefits of a self driving car and an insight into the technology behind it.

### HOW?
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
 
 
 ### HOW TO SET UP 
 
 #### Dependencies
  - Hardware listed above
  - Phone powered by Android (version 4.3+)
  - Android Studio
  - Arduino IDE
  
  #### Where to start
  
 Clone this repository by opening your terminal, navigating to your favourite folder and typing:
    
  ```
  git clone https://github.com/DIT112-V19/group-10.git
  ```
  
  #### Car
  
  If you like our product and feel an unresistable urge to build the car from scratch, the best way for you is to visit the [github repository of Dimitris Platis](https://github.com/platisd/smartcar_shield) that has extensive documentation on the harware of this car and the library we used.
  
  Once you have the hardware assembled, you will need to upload the ```master.ino``` sketch to your car. If you have never worked with Arduino yet, here is our guideline:
  
  1. Open Arduino IDE
      
      If you don't have it yet, it's available from [this website](https://www.arduino.cc/en/Main/Software).
      
  2. You will need to import the code to the IDE from the src folder of this project
  
      This is the perfect moment to remember which folder did you clone this repository.
      
  3. You will likely need to import the Smartcar Shield library
  
       This step by step [guideline](https://www.ardu-badge.com/Smartcar%20shield) will help!
       
   4. Assuming that you have the USB cable for the Arduino board, connect the board to your computer
   
       If it starts smoking or your computer dies, you did something wrong.
       
   5. Hit Verify and Upload buttons (in this order)
   
   That's about it! Let's move on to the settings of the app.
  
  
  #### App
 
 Our app is currently not available from any app store, but if you enjoy putting together an arduino car you will surely enjoy entering your smartphone as a developer.
 
 All you need to do is to follow this step-by-step guideline:

1. Open your Android studio

     If you don't have it yet, you can download from the [Android Studio website](https://developer.android.com/studio).
  
2. Import project to Android studio from your favourite folder
  
3. Connect your phone via USB and enable file transfer

     If you haven't set up developer mode on your phone yet, you can find the guidelines [here](https://www.digitaltrends.com/mobile/how-to-get-developer-options-on-android/).
  
4. Run the project from Android studio and select your phone from the given list

You're all set. Enjoy!

 ### FOR DEVELOPERS
 
 The high-level overview of our two-tier system is depicted in the picture below:
![deployment diagram](https://github.com/DIT112-V19/group-10/blob/documentation/DeploymentDiagram.png)

 #### Before we begin
  
  We love open source software, so all of our code can be found in this repository. We ensure by using Travis CI that the code you find on Master branch always a usable version of our product. We also encourage you to contribute to our project by opening a new issue, adding more functions or give a feedback.
  
  Note: If you chose to contribute, you will not be able to push to master unless the code compiles, all of our tests pass and at least another contributing member approved your code. But do not let this discourage you, on the contrary: mess as much as you want, we're all safe!
  
  #### Where to start
  ...
  
  ### REFERENCE / DOCUMENTATION

# Group 10

## Project Proposal

Our product aims to simulate a real-world driving experience and introduces the user to some aspects of self driving cars. We want this technology to be accessible as we believe that anyone should be able to experience the benefits of a self driving car and an insight into the technology behind it.

This product works on two platforms: an Arduino car and a mobile application.
The car will be able to drive autonomously by following a lane and avoiding obstacles. Using a mobile app, the user will be able to manually control the car. Both the car and the app will have security measures, to prevent unauthorized access to the car. The app will allow the user to easily switch between autonomous and manual mode.


### Features
##### Obstacle avoidance
This ensures the car does not crash when in autonomous mode. Using the ultrasonic sensor, the car will always be a reasonable distance away from any obstacle. Once an obstacle has been detected, the car will drive around it and return to its course. It will be a challenge to ensure the car reacts quickly to an object in its path.

##### Lane detection
This feature allows the car to drive itself around a given course, staying within the lane. Using multiple infrared sensors, the car will be centered on the track. We anticipate a challenge with ensuring the car moves as expected around turns and bends.

##### Speed control
The app will give the user the opportunity to control the speed of the car, using pre-built controls. The range will be from zero to maximum speed. This gives the user more control over the car and is available across both modes. The challenge here would be to make sure that the input from the user is able to quickly affect the speed of the car.

##### Steering
This feature gives the user manual control over the car. Using the app, the user can steer the car by adjusting the speed of the wheels on either side to turn. It will be challenging to ensure the accuracy of the angle the car is turning so that it turns as expected.

##### Security
This is an important feature because the car should only be driven by authorized users. The app will have a password/fingerprint scanner to allow access. The main challenge would be to make certain that this feature is effective, as we are only implementing it on one platform.

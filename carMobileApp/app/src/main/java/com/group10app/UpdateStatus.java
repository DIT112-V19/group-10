package com.group10app;

public class UpdateStatus {

  // Toggle between START and STOP
  private static boolean isStopped = true;
  private static final String START = "start";
  private static final String STOP = "stop";

  //Current speed and distance travelled
  private static int currentSpeed  = 0; // cm/s
  private static int distanceTravelled  = 0; // cm

  public String getButton(Boolean currentButtonState) {
    if (currentButtonState) {
      isStopped = false;
      return STOP;
    } else {
      isStopped = true;
      return START;
    }
  }

  public int getSpeed() {
    return currentSpeed;
  }
}
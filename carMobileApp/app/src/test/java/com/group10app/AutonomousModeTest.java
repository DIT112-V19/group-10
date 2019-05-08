package com.group10app;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class AutonomousModeTest {

  private static AutonomousMode testMode;

  @BeforeClass
  public static void SetUpClass(){
    testMode = new AutonomousMode();
  }

  @Test
  public void StartCarWhenButtonPressedIfCarNotRunning(){
    testMode.setIsStopped(true);

    boolean actualValue = testMode.buttonPress();
    assertFalse(actualValue);
  }

  @Test
  public void StopCarWhenButtonPressedIfCarRunning(){
    testMode.setIsStopped(false);

    boolean actualValue = testMode.buttonPress();
    assertTrue(actualValue);
  }
}
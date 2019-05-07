package com.group10app;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AutonomousModeInstrumentedTest {

  private AutonomousMode autonomousMode;

  @Rule
  public ActivityTestRule<AutonomousMode> mActivityRule =
      new ActivityTestRule<>(AutonomousMode.class);


  public AutonomousModeInstrumentedTest() {
    this.autonomousMode = new AutonomousMode();
  }

  @BeforeClass
  public static void SetUpClass() {
    // Context of the app under test.
    InstrumentationRegistry.getTargetContext();
  }

  @Test
  public void CheckIfObstacleDetectedIsFalseAtStart(){
    assertFalse(autonomousMode.getObstacleDetected());
  }
}
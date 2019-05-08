package com.group10app;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.v4.content.ContextCompat.startActivity;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AutonomousModeInstrumentedTest {

  @Rule
  public ActivityTestRule<AutonomousMode>  mAutonomousModeTestRule=
    new ActivityTestRule<>(AutonomousMode.class, true ,true );

  @BeforeClass
  public static void SetUpClass(){
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getTargetContext();
  }

  @Test
  public void CheckIfObstacleDetectedIsFalseWhenSet(){

    mAutonomousModeTestRule.getActivity().setObstacleDetected(false);
    assertFalse(mAutonomousModeTestRule.getActivity().getObstacleDetected());
  }


}

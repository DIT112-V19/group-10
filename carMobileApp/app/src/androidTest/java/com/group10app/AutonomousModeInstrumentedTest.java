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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.fail;


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

    @Test
    public void ButtonTextAndStatusUpdateMustChangeAfterStoppingCar(){

        // when no obstacle is detected
      mAutonomousModeTestRule.getActivity().setObstacleDetected(false);
        // and the car is not stopped
      mAutonomousModeTestRule.getActivity().setIsStopped(false);
        // the expected status is "start" after pushing the button
        String expectedButtonStatus = "start";
        String expectedUpdateStatus = "stopped";

        // button clicked, button text checked
        onView(withId(R.id.button)).perform(click()).check(matches(withText(expectedButtonStatus)));
        // updateStatus text checked
        onView(withId(R.id.updatedStatus)).check(matches(withText(expectedUpdateStatus)));
    }

    @Test
    public void ButtonTextAndStatusUpdateMustChangeAfterStartingCar(){
        // when no obstacle is detected
      mAutonomousModeTestRule.getActivity().setObstacleDetected(false);
        // and the car is stopped
      mAutonomousModeTestRule.getActivity().setIsStopped(true);
        // the expected status is "stop" after pushing the button
        String expectedButtonStatus = "stop";
        String expectedUpdateStatus = "driving";

        // button clicked, button text checked
        onView(withId(R.id.button)).perform(click()).check(matches(withText(expectedButtonStatus)));
        // updateStatus text checked
        onView(withId(R.id.updatedStatus)).check(matches(withText(expectedUpdateStatus)));
    }

    @Test
    public void ButtonTextAndStatusUpdateMustChangeAfterObstacleDetected(){
        // when no obstacle is detected
      mAutonomousModeTestRule.getActivity().setObstacleDetected(true);
        // and the car is stopped
      mAutonomousModeTestRule.getActivity().setIsStopped(true);
        // the expected status is "stop" after pushing the button
        String expectedButtonStatus = "stop";
        String expectedUpdateStatus = "obstacle detected";

        // button clicked, button text checked
        onView(withId(R.id.button)).perform(click()).check(matches(withText(expectedButtonStatus)));
        // updateStatus text checked
        onView(withId(R.id.updatedStatus)).check(matches(withText(expectedUpdateStatus)));

        // the status update must remain the same even if button pressed
        onView(withId(R.id.button)).perform(click());
        // updateStatus text checked
        onView(withId(R.id.updatedStatus)).check(matches(withText(expectedUpdateStatus)));
    }

}

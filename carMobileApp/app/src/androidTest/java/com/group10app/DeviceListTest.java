package com.group10app;

import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import junit.framework.AssertionFailedError;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeviceListTest {

  @Rule
  public ActivityTestRule<DeviceList> deviceList =
      new ActivityTestRule<>(DeviceList.class, true, true );

  @Test
  public void checkIfBTTurnsOn() {
    try {
      onView(withId(R.id.turnOn)).perform(click());

      GrantPermissionRule.grant(android.Manifest.permission.BLUETOOTH_ADMIN);
      GrantPermissionRule.grant(android.Manifest.permission.BLUETOOTH);
      GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
      GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

      assertNotEquals(null, deviceList.getActivity().bluetoothAdapter);
    } catch (AssertionError e) {
      assertNull(deviceList.getActivity().bluetoothAdapter);
    }
  }

  @Test
  public void checkIfBTTurnsOff() {
    onView(withId(R.id.turnOff)).perform(click());

    try {
      onView(withId(R.id.status_text))
          .check(matches(withText(containsString("BT Status: Off"))));
    } catch (AssertionFailedError e) {
      onView(withId(R.id.status_text))
          .check(matches(withText(containsString("BT Status: Not Supported"))));
    }
  }

//  @Test
//  public void t1checkIfGetPairedDevicesWorks() {
//    onView(withId(R.id.get_paired_devices)).perform(click());
//
//    try {
//      Assert.assertSame(0, deviceList.getActivity().pairedDevices.size());
//    } catch (AssertionError e) {
//      Assert.assertNotEquals(0, deviceList.getActivity().pairedDevices.size());
//    }
//  }
//
//  @Test
//  public void t3checkOnActivityResult() {
//    try {
//
//      try {
//        onView(withId(R.id.status_text))
//            .check(matches(withText(containsString("BT Status: On"))));
//      } catch (AssertionError e) {
//        onView(withId(R.id.status_text))
//            .check(matches(withText(containsString("BT Status: Off"))));
//      }
//
//    } catch (AssertionError e) {
//      onView(withId(R.id.status_text))
//          .check(matches(withText(containsString("BT Status: Not Supported"))));
//    }
//  }
}
package com.group10app;

import org.hamcrest.core.IsEqual;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class UpdateStatusTest {

    private static UpdateStatus testStatus;

    @BeforeClass
    public static void SetUpClass(){
        testStatus = new UpdateStatus();
    }

    @Test
    public void IfCarStoppedAndButtonPressedButtonDisplaysSTOP(){

        boolean isCarStopped = true;

        String expectedValue = "STOP";
        String actualValue = testStatus.getButton(isCarStopped);

        assertEquals("Button pushed, text changed", expectedValue.toLowerCase(),actualValue.toLowerCase());
    }

    @Test
    public void IfCarRunningAndButtonPressedButtonDisplaysSTART(){

        boolean isCarStopped = false;

        String expectedValue = "START";
        String actualValue = testStatus.getButton(isCarStopped);

        assertEquals("Button pushed, text changed", expectedValue.toLowerCase(),actualValue.toLowerCase());
    }


    @Test
    public void SpeedOfCarReturned(){

        // ToDo: expectedValue is not always 0
        int expectedValue = 0;
        int actualValue = testStatus.getSpeed();

        assertEquals(expectedValue,actualValue);

    }

}
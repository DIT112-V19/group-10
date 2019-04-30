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
        AutonomousMode.setIsStopped(true);

        boolean expectedValue = false;
        boolean actualValue = testMode.buttonPress();

        assertEquals(expectedValue,actualValue);
    }

    @Test
    public void StopCarWhenButtonPressedIfCarRunning(){
        AutonomousMode.setIsStopped(false);

        boolean expectedValue = true;
        boolean actualValue = testMode.buttonPress();

        assertEquals(expectedValue,actualValue);
    }

}

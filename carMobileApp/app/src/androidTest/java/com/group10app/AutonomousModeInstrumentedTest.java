package com.group10app;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AutonomousModeInstrumentedTest{

    @BeforeClass
    public static void SetUpClass(){
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void CheckIfObstacleDetectedIsFalseAtStart(){

        assertEquals(false,AutonomousMode.getObstacleDetected());
    }

}

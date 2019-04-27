package com.group10app;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AutonomousModeInstrumentedTest{

    private MainActivity mActivity;
    private AutonomousMode testMode;


    protected void setUp() throws Exception{
        testMode = new AutonomousMode();
    }

    @Test
    public void DisplayedTextTest(){

    }

}

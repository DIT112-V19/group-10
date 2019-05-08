package com.group10app;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ImageView;

import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ManualModeInstrumentedTest {

  @Rule
  public ActivityTestRule<ManualMode> manualModeActivityTestRule=
    new ActivityTestRule<>(ManualMode.class, true ,true );


  @Test
  public void testFwdOnClickListener(){

    // fwdArrow is false before clicked
    boolean fwdArrowTest = manualModeActivityTestRule.getActivity().getFwdArrowClicked();
    assertFalse(fwdArrowTest);

    // fwdArrow is true after clicked
    manualModeActivityTestRule.getActivity().getFwdArrow().callOnClick();
    fwdArrowTest = manualModeActivityTestRule.getActivity().getFwdArrowClicked();
    assertTrue(fwdArrowTest);

  }


  @Test
  public void testBckwOnClickListener(){

    // bckwArrow is false before clicked
    boolean bckwArrowTest = manualModeActivityTestRule.getActivity().getBckwArrowClicked();
    assertFalse(bckwArrowTest);

    // bckwArrow is true after clicked
    manualModeActivityTestRule.getActivity().getBckwArrow().callOnClick();
    bckwArrowTest = manualModeActivityTestRule.getActivity().getBckwArrowClicked();
    assertTrue(bckwArrowTest);

  }

  @Test
  public void testLeftOnClickListener(){

    // leftArrow is false before clicked
    boolean leftArrowTest = manualModeActivityTestRule.getActivity().getLeftArrowClicked();
    assertFalse(leftArrowTest);

    // bckwArrow is true after clicked
    manualModeActivityTestRule.getActivity().getLeftArrow().callOnClick();
    leftArrowTest = manualModeActivityTestRule.getActivity().getLeftArrowClicked();
    assertTrue(leftArrowTest);

  }

  @Test
  public void testRightOnClickListener(){

    // rightArrow is false before clicked
    boolean rightArrowTest = manualModeActivityTestRule.getActivity().getRightArrowClicked();
    assertFalse(rightArrowTest);

    // rightArrow is true after clicked
    manualModeActivityTestRule.getActivity().getRightArrow().callOnClick();
    rightArrowTest = manualModeActivityTestRule.getActivity().getRightArrowClicked();
    assertTrue(rightArrowTest);

  }
}

package com.skrebe.titas.grabble;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ApplicationTest {

    @Rule
    public ActivityTestRule<MapsActivity> mActivityRule = new ActivityTestRule(MapsActivity.class);


    @Test
    public void listGoesOverTheFold() {
        onView(withText("Statistics")).check(matches(isDisplayed()));
    }

    @Rule
    public ActivityTestRule<LetterActivity> letterActivity = new ActivityTestRule(LetterActivity.class);

    @Test
    public void checkLetterBank(){
        onView(withId(R.id.autoCompleteTextView))
                .perform(typeText("john"))
                .check(matches(isDisplayed()));
    }
}
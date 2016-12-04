package com.skrebe.titas.grabble;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddWordTest {

    @Rule
    public ActivityTestRule<PermissionsActivity> mActivityTestRule = new ActivityTestRule<>(PermissionsActivity.class);

    @Test
    public void addWordTest() {

        onView(
                allOf(withText("Add a word"),
                        withParent(allOf(withId(R.id.map),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed())).perform(click());

        onView(
                allOf(withId(R.id.autoCompleteTextView), isDisplayed()))
                .perform(replaceText("sunburn"), closeSoftKeyboard());

        onView(
                allOf(withId(R.id.autoCompleteTextView), withText("sunburn"), isDisplayed()))
                .perform(click());

        onView(
                allOf(withId(R.id.addWord), withText("Add"), isDisplayed()))
                .perform(click()).check(matches(hasErrorText("Collect more letters")));

        onView(withText("Collect more letters")).check(matches(isDisplayed()));


    }

}

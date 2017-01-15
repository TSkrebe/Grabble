package com.skrebe.titas.grabble;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RandomTest {

    @Rule
    public ActivityTestRule<PermissionsActivity> mActivityTestRule = new ActivityTestRule<>(PermissionsActivity.class);

    @Test
    public void randomTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        withParent(allOf(withId(R.id.clayout),
                                withParent(withId(R.id.map)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.fab),
                        withParent(allOf(withId(R.id.clayout),
                                withParent(withId(R.id.map)))),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction button = onView(
                allOf(withText("Add a word"),
                        withParent(allOf(withId(R.id.map),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction customAutoTextView = onView(
                allOf(withId(R.id.autoCompleteTextView), isDisplayed()));
        customAutoTextView.perform(click());

        ViewInteraction customAutoTextView2 = onView(
                allOf(withId(R.id.autoCompleteTextView), isDisplayed()));
        customAutoTextView2.perform(replaceText("da"), closeSoftKeyboard());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText("damning"), isDisplayed()));
        appCompatTextView.perform(click());

        pressBack();

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.addWord), withText("Add"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction button2 = onView(
                allOf(withText("Statistics"),
                        withParent(allOf(withId(R.id.map),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button2.perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(withId(R.id.statsToolbar)),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

    }

}

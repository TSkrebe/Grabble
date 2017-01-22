package com.skrebe.titas.grabble;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Goes to "Add a word" activity
 * Writes a word and click "add" button
 * Goes back to map
 * Goes to statistics activity
 * Goes to settings activity
 * Changes difficulty to Easy
 * Goes back to map
 * Clicks on FAB
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class FlowTest {

    @Rule
    public ActivityTestRule<PermissionsActivity> mActivityTestRule = new ActivityTestRule<>(PermissionsActivity.class);

    @Test
    public void flowTest() {
        ViewInteraction button = onView(
                allOf(withText("Add a word"),
                        withParent(allOf(withId(R.id.map),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button.perform(click());

        onView(
                allOf(withId(R.id.autoCompleteTextView), isDisplayed()))
        .perform(replaceText("thallus"), closeSoftKeyboard());

        onView(
                allOf(withText("thallus"), isDisplayed()))
        .perform(click());

        onView(
                allOf(withId(R.id.addWord), withText("Add"), isDisplayed())).perform(click());

        onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()))
                .perform(click());

        onView(
                allOf(withText("Statistics"),
                        withParent(allOf(withId(R.id.map),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()))
                .perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(
                allOf(withId(R.id.title), withText("Settings"), isDisplayed()))
                .perform(click());

        onView(
                allOf(childAtPosition(
                        withId(android.R.id.list),
                        0),
                        isDisplayed()))
                .perform(click());

        onView(
                allOf(withId(android.R.id.text1), withText("Easy"),
                        childAtPosition(
                                allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                                        withParent(withClassName(is("android.widget.FrameLayout")))),
                                0),
                        isDisplayed())).perform(click());

        onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(allOf(withId(R.id.settingsToolbar),
                                withParent(withId(R.id.settingsToolbarParent)))),
                        isDisplayed()))
                .perform(click());

        onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(withId(R.id.statsToolbar)),
                        isDisplayed()))
                .perform(click());

        onView(
                allOf(withId(R.id.fab),
                        withParent(allOf(withId(R.id.clayout),
                                withParent(withId(R.id.map)))),
                        isDisplayed()))
                .perform(click());


    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

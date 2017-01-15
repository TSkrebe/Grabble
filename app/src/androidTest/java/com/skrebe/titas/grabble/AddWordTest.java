package com.skrebe.titas.grabble;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.skrebe.titas.grabble.helpers.DatabaseHelper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddWordTest {

    @Rule
    public ActivityTestRule<PermissionsActivity> mActivityTestRule = new ActivityTestRule<>(PermissionsActivity.class);


    @Test
    public void addWordTest() {

        //try to add "sunburn" -> collect more letters
        tryToAddWord("sunburn", "Collect more letters");

        //try to add "sun" -> collect more letters
        tryToAddWord("sun", "A word must have 7 letters");

        //add more letters so we can add sunburn
        addLetters("sunburn");

        //should be OK
        tryToAddWord("sunburn", "Word sunburn was added");

        //check if the word was added
        checkStatsActivity("sunburn");

    }

    private void checkStatsActivity(String word) {

        onView(withText("Statistics")).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.wordList))
                .atPosition(0)
                .onChildView(withId(R.id.word))
                .check(matches(withText(word))).perform(click());
    }

    private void addLetters(String sunburn) {

        DatabaseHelper db = new DatabaseHelper(mActivityTestRule.getActivity().getApplicationContext());

        for (int i = 0; i < sunburn.length(); i++){
            db.addLetter(sunburn.charAt(i)+"", 1);
        }
    }

    private void tryToAddWord(String word, String result) {

        onView(
                allOf(withText("Add a word"),
                        withParent(allOf(withId(R.id.map),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed())).perform(click());

        onView(
                allOf(withId(R.id.autoCompleteTextView), isDisplayed()))
                .perform(replaceText(word), closeSoftKeyboard());

        onView(
                allOf(withId(R.id.autoCompleteTextView), withText(word), isDisplayed()))
                .perform(click());

        onView(
                allOf(withId(R.id.addWord), withText("Add"), isDisplayed()))
                .perform(click());

        onView(withText(result)).check(matches(isDisplayed()));
        onView(allOf(withContentDescription("Navigate up"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()))
                .perform(click());
    }



}

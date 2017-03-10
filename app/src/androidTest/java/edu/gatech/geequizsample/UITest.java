package edu.gatech.geequizsample;

import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.anything;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITest {
    @Rule
    public final ActivityTestRule<StartActivity> main = new ActivityTestRule<>(StartActivity.class);

    @Test
    public void dashboard_checkCategory() {
        onView(withId(R.id.dashboard)).perform(click());
        // Check if a particular category appears in the Dashboard
        onView(withText("C Programming Mock Tests")).check(matches(isDisplayed()));
        onView(withText("Theory of Computation Mock Tests")).check(matches(isDisplayed()));
    }

    @Test
    public void dashboard_viewScores() {
        onView(withId(R.id.dashboard)).perform(click());
        //all the categories should be listed
        onData(anything()).inAdapterView(withId(R.id.categories)).atPosition(0).perform(click());
        onView(withText("C Programming Mock Tests")).check(matches(isDisplayed()));
        onView(withContentDescription("c0_score")).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.categories)).atPosition(1).perform(click());
        onView(withText("C++ Programming Mock Tests")).check(matches(isDisplayed()));
        onView(withContentDescription("c1_score")).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.categories)).atPosition(2).perform(click());
        onView(withText("Engineering Mathematics")).check(matches(isDisplayed()));
        onView(withContentDescription("c2_score")).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.categories)).atPosition(3).perform(click());
        onView(withText("Computer Organization and Architecture")).check(matches(isDisplayed()));
        onView(withContentDescription("c3_score")).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.categories)).atPosition(4).perform(click());
        onView(withText("Data Structures Mock Tests")).check(matches(isDisplayed()));
        onView(withContentDescription("c4_score")).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.categories)).atPosition(5).perform(click());
        onView(withText("Java Programming Mock Tests")).check(matches(isDisplayed()));
        onView(withContentDescription("c5_score")).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.categories)).atPosition(6).perform(click());
        onView(withText("Theory of Computation Mock Tests")).check(matches(isDisplayed()));
        onView(withContentDescription("c6_score")).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.categories)).atPosition(7).perform(click());
        onView(withText("GATE Mock Tests")).check(matches(isDisplayed()));
        onView(withContentDescription("c7_score")).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.categories)).atPosition(8).perform(click());
        onView(withText("Algorithms Mock Tests")).check(matches(isDisplayed()));
        onView(withContentDescription("c8_score")).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.categories)).atPosition(9).perform(click());
        onView(withText("Operating Systems Mock Tests")).check(matches(isDisplayed()));
        onView(withContentDescription("c9_score")).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.categories)).atPosition(10).perform(click());
        onView(withText("DBMS Mock Tests")).check(matches(isDisplayed()));
        onView(withContentDescription("c10_score")).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.categories)).atPosition(11).perform(click());
        onView(withText("Computer Networks Mock Tests")).check(matches(isDisplayed()));
        onView(withContentDescription("c11_score")).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.categories)).atPosition(12).perform(click());
        onView(withText("Aptitude Mock Tests")).check(matches(isDisplayed()));
        onView(withContentDescription("c12_score")).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.categories)).atPosition(13).perform(click());
        onView(withText("Other Topics in Computer Science")).check(matches(isDisplayed()));
        onView(withContentDescription("c13_score")).check(matches(isDisplayed()));
    }

    @Test
    public void quiz_move() {
        onView(withId(R.id.take_quiz)).perform(click());
        onView(withText("Engineering Mathematics")).perform(click());
        onView(withContentDescription("q0_o1")).check(matches(isDisplayed()));

        onView(withContentDescription("q0_o1")).perform(scrollTo(), click(), swipeLeft());
        SystemClock.sleep(500);
        onView(withContentDescription("q1_o1")).check(matches(isDisplayed()));

        onView(withContentDescription("q1_o1")).perform(scrollTo(), click(), swipeRight());
        SystemClock.sleep(500);
        onView(withContentDescription("q0_o1")).check(matches(isDisplayed()));
    }

    @Test
    public void quiz_TakeRandomQuiz() {
        onView(withId(R.id.take_quiz)).perform(click());
        onView(withText("C Programming Mock Tests")).perform(click());

        for(int i = 0; i < 10; i++) {
            onView(withContentDescription("q" + i + "_o0")).perform(scrollTo(), click(), swipeLeft());
            SystemClock.sleep(500);
        }

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Finish")).perform(click());
        SystemClock.sleep(500);

        // Check if on results screen and SCORE is displayed
        onView(withId(R.id.sessionSummary)).check(matches(isDisplayed()));
        onView(withId(R.id.explanation_question_text)).check(matches(isDisplayed()));
        onView(withId(R.id.explanation_wrong_text)).check(matches(isDisplayed()));
        onView(withId(R.id.explanation_correct_text)).check(matches(isDisplayed()));
        onView(withId(R.id.explanation_text)).check(matches(isDisplayed()));

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Finish")).check(matches(isDisplayed()));

    }

    @Test
    public void quiz_TakeSameRandomQuiz() {
        onView(withId(R.id.take_quiz)).perform(click());
        onView(withText("Theory of Computation Mock Tests")).perform(click());

        onView(withContentDescription("q0_o0")).perform(scrollTo(), click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Finish")).perform(click());
        SystemClock.sleep(500);

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Finish")).perform(click());
        SystemClock.sleep(500);

        onView(withText("Theory of Computation Mock Tests")).perform(click());
        SystemClock.sleep(500);

        onView(withText("Start a new session?")).check(matches(isDisplayed()));
    }


}

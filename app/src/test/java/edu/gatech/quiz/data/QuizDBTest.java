package edu.gatech.quiz.data;

import static org.junit.Assert.*;

import edu.gatech.geequizsample.BuildConfig;
import edu.gatech.quiz.LocalQuizDB;
import edu.gatech.quiz.helpers.Option;
import edu.gatech.quiz.helpers.Question;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.*;

/**
 * CS3300 Project 2
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class QuizDBTest {

    // Use local DB while running unit test
    LocalQuizDB db;

    @Before
    public void setUp() {
        db = new LocalQuizDB();
        db.setLocalDatabaseFile();
    }

    @Test
    public void testCategoryLoading() {
        List<String> categories = db.getCategories();
        assertEquals(14, categories.size());
        assertTrue(categories.contains(
                "Theory of Computation Mock Tests"));
        assertTrue(categories.contains(
                "C Programming Mock Tests"));
        assertFalse(categories.contains(""));
    }

    @Test
    public void testQuestionLoading1() {
        List<Question> questions = db.getCategoryQuestions(
                "Theory of Computation Mock Tests");
        assertEquals(4, questions.size());
    }

    @Test
    public void testQuestionLoading2() {
        List<Question> questions = db.getCategoryQuestions(
                "C Programming Mock Tests");
        assertEquals(214, questions.size());
    }

    @Test
    public void testQuestionLoading3() {
        List<Question> questions = db.getCategoryQuestions(
                "Invalid Category");
        assertNull(questions);
    }

    @Test
    public void testQuestionValidity1() {
        List<Question> questions = db.getCategoryQuestions(
                "C Programming Mock Tests");
        for (Question q : questions) {
            assertNotEquals("", q.getBodyText());
        }
    }

    @Test
    public void testQuestionValidity2() {
        List<Question> questions = db.getCategoryQuestions(
                "Theory of Computation Mock Tests");
        for (Question q : questions) {
            assertNotEquals("", q.getBodyText());
        }
    }

    @Test
    public void testOptions1() {
        List<Question> questions = db.getCategoryQuestions(
                "C Programming Mock Tests");
        for (Question q : questions) {
            assertNotEquals(0, q.getOptions().size());
        }
    }

    @Test
    public void testOptions2() {
        List<Question> questions = db.getCategoryQuestions(
                "Theory of Computation Mock Tests");
        for (Question q : questions) {
            assertNotEquals(0, q.getOptions().size());
        }
    }

    @Test
    public void testCorrectAnswer1() {
        List<Question> questions = db.getCategoryQuestions(
                "C Programming Mock Tests");
        for (Question q : questions) {
            List<Option> correctAnswers = new ArrayList<Option>();

            for (Option o : q.getOptions()) {
                if (o.isCorrect()) {
                    correctAnswers.add(o);
                }
            }

            assertEquals(1, correctAnswers.size());
        }
    }

    @Test
    public void testCorrectAnswer2() {
        List<Question> questions = db.getCategoryQuestions(
                "Theory of Computation Mock Tests");
        for (Question q : questions) {
            List<Option> correctAnswers = new ArrayList<Option>();

            for (Option o : q.getOptions()) {
                if (o.isCorrect()) {
                    correctAnswers.add(o);
                }
            }

            assertEquals(1, correctAnswers.size());
        }
    }
}
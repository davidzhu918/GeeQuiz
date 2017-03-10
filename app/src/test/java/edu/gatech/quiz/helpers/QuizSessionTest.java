package edu.gatech.quiz.helpers;

import edu.gatech.geequizsample.BuildConfig;
import edu.gatech.quiz.LocalQuizDB;
import edu.gatech.quiz.helpers.Question;
import edu.gatech.quiz.helpers.QuizSession;
import static org.junit.Assert.*;

import edu.gatech.quiz.helpers.*;
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
public class QuizSessionTest {

    // Use local DB while running unit test
    LocalQuizDB db;

    @Before
    public void setUp() {
        db = new LocalQuizDB();
        db.setLocalDatabaseFile();
    }

    @Test
    public void testShortSession1() {
        QuizSession session = QuizSession.createShortSession(
                "Theory of Computation Mock Tests", db);
        int size = session.getQuestions().size();
        assertTrue(size > 0);
        assertTrue(size <= 10);
    }

    @Test
    public void testShortSession2() {
        QuizSession session = QuizSession.createShortSession(
                "C Programming Mock Tests", db);
        int size = session.getQuestions().size();
        assertTrue(size > 0);
        assertTrue(size <= 10);
    }

    @Test
    public void testLongSession() {
        QuizSession session = QuizSession.createLongSession(
                "C Programming Mock Tests", db);
        assertEquals(214, session.getQuestions().size());
    }

    @Test
    public void testOptions() {
        QuizSession session = QuizSession.createShortSession(
                "Theory of Computation Mock Tests", db);

        List<Question> questions = session.getQuestions();
        for (Question q : questions) {
            List<Option> options = q.getOptions();
            assertTrue(options.size() > 0);

            List<Option> correctAnswers = new ArrayList<Option>();
            for (Option opt : options) {
                if (opt.isCorrect()) {
                    correctAnswers.add(opt);
                }
            }

            assertEquals(1, correctAnswers.size());
        }
    }

    @Test
    public void testSetUserAnswer() {
        QuizSession session = QuizSession.createShortSession(
                "Theory of Computation Mock Tests", db);
        List<Question> questions = session.getQuestions();
        Question q = questions.get(0);
        Option o = q.getOptions().get(0);

        session.setUserAnswer(q, o);
        assertEquals(o, session.getUserAnswer(q));
    }

    @Test
    public void testSolvedAll() {
        QuizSession session = QuizSession.createShortSession(
                "Theory of Computation Mock Tests", db);
        List<Question> questions = session.getQuestions();
        for (int i = 0; i < questions.size() - 1; ++i) {
            Question q = questions.get(i);
            session.setUserAnswer(q,
                    q.getOptions().get(0));
            assertFalse(session.solvedAll());
        }

        Question q = questions.get(questions.size() - 1);
        session.setUserAnswer(q,
                q.getOptions().get(0));
        assertTrue(session.solvedAll());
    }

    @Test
    public void testGetScore() {
        QuizSession session = QuizSession.createShortSession(
                "Theory of Computation Mock Tests", db);
        for (Question q : session.getQuestions()) {
            List<Option> options = q.getOptions();
            String text = q.getBodyText();
            Option answer;

            if (text.startsWith("Which one of the following is FALSE?")) {
                answer = findOptionThatStartsWith(options,
                        "There is unique");
            } else if (text.startsWith("Let Nf and")) {
                answer = findOptionThatStartsWith(options,
                        "Df ⊂ Nf and Dp ⊂ Np");
            } else if (text.startsWith("Given the language")) {
                answer = findOptionThatStartsWith(options,
                        "1, 2 and 3");
            } else {
                answer = findOptionThatStartsWith(options,
                        "The set of all strings containing at least two 0’s");
            }

            assertNotNull(answer);
            session.setUserAnswer(q, answer);
        }

        assertEquals(1, session.getScore());
    }

    private Option findOptionThatStartsWith(List<Option> options, String s) {
        for (Option opt : options) {
            if (opt.getOptionText().startsWith(s)) {
                return opt;
            }
        }

        return null;
    }
}
package edu.gatech.quiz.TaskCards;

/**
 * Created by zixiangzhu on 11/26/16.
 */

import edu.gatech.geequizsample.BuildConfig;
import edu.gatech.quiz.LocalQuizDB;
import edu.gatech.quiz.helpers.Question;
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
public class SessionMgrTest {
    // Use local DB while running unit test
    LocalQuizDB db;

    @Before
    public void setUp() {
        db = new LocalQuizDB();
        db.setLocalDatabaseFile();
    }

    @Test
    public void testHasNextQuestion() {
        SessionMgr mgr = new SessionMgr("Theory of Computation Mock Tests", db);
        assertTrue(mgr.hasNextQuestion());

        mgr = new SessionMgr("", db);
        assertFalse(mgr.hasNextQuestion());
    }

    @Test
    public void testGetNextQuestion() {
        SessionMgr mgr = new SessionMgr("Theory of Computation Mock Tests", db);
        Question q1 = mgr.getNextQuestion();
        assertNotNull(q1);

        Question q2 = mgr.getNextQuestion();
        assertNotNull(q2);

        assertNotEquals(q1.getId(), q2.getId());
    }

    @Test
    public void testHasPrevQuestion() {
        SessionMgr mgr = new SessionMgr("Theory of Computation Mock Tests", db);
        mgr.getNextQuestion();
        assertFalse(mgr.hasPreviousQuestion());

        while(mgr.hasNextQuestion()) {
            mgr.getNextQuestion();
            assertTrue(mgr.hasPreviousQuestion());
        }
    }

    @Test
    public void testGetPrevQuestion() {
        SessionMgr mgr = new SessionMgr("Theory of Computation Mock Tests", db);
        mgr.getNextQuestion();
        mgr.getNextQuestion();
        mgr.getNextQuestion();

        Question q1 = mgr.getPreviousQuestion();
        assertNotNull(q1);

        Question q2 = mgr.getPreviousQuestion();
        assertNotNull(q2);

        assertNotEquals(q1.getId(), q2.getId());

        assertFalse(mgr.hasPreviousQuestion());
    }

    @Test
    public void testGetNumberQuestions1() {
        SessionMgr mgr = new SessionMgr("Theory of Computation Mock Tests", db);
        int total = mgr.getNumberQuestions();
        assertTrue(total > 0);
        assertTrue(total <= 10);
    }

    @Test
    public void testGetNumberQuestions2() {
        SessionMgr mgr = new SessionMgr("C Programming Mock Tests", db);
        int total = mgr.getNumberQuestions();
        assertTrue(total > 0);
        assertTrue(total <= 10);
    }

    @Test
    public void testSetAnswerAndCorrectBefore1() {
        //the only way to check if correct answer is set is
        // to check if it is recorded in correct set
        SessionMgr mgr = new SessionMgr("C Programming Mock Tests", db);
        Question question = mgr.getNextQuestion();
        Option op = getCorrectOption(question);
        mgr.setAnswer(question, op);
        assertTrue(mgr.correctBefore(question));

        //use wrong answer iterator
        QuestionIterable iterator = mgr.getIteratorWrong();
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testSetAnswerAndCorrectBefore2() {
        //the only way to check if correct answer is set is
        // to check if it is recorded in correct set
        SessionMgr mgr = new SessionMgr("C Programming Mock Tests", db);
        Question question = mgr.getNextQuestion();
        int oId = 0;
        for(int i = 0; i < question.getOptions().size(); i++) {
            if(question.getOptions().get(i).isCorrect()) {
                oId = i;
            }
        }

        mgr.setAnswer(0, oId);

        assertTrue(mgr.correctBefore(question));

        //use wrong answer iterator
        QuestionIterable iterator = mgr.getIteratorWrong();
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testGetIteratorWrong() {
        SessionMgr mgr = new SessionMgr("C Programming Mock Tests", db);
        Question question = mgr.getNextQuestion();
        Option op = getIncorrectOption(question);
        mgr.setAnswer(question, op);

        assertFalse(mgr.correctBefore(question));

        //use wrong answer iterator
        QuestionIterable iterator = mgr.getIteratorWrong();
        assertTrue(iterator.hasNext());
        assertEquals(question.getId(), iterator.next().getId());
    }

    @Test
    public void testGetNumberCorrectQuestions() {
        SessionMgr mgr = new SessionMgr("C Programming Mock Tests", db);
        Question question = mgr.getNextQuestion();
        Option op = getCorrectOption(question);
        mgr.setAnswer(question, op);

        assertEquals(mgr.getNumberCorrectQuestions(), 1);
    }

    @Test
    public void testGetQuestionState() {
        SessionMgr mgr = new SessionMgr("C Programming Mock Tests", db);
        mgr.getNextQuestion();

        Question q2 = mgr.getNextQuestion();
        mgr.setAnswer(q2, getCorrectOption(q2));

        Question q3 = mgr.getNextQuestion();
        mgr.setAnswer(q3, getIncorrectOption(q3));

        assertEquals(QuestionState.BLANK, mgr.getQuestionState(0));
        assertEquals(QuestionState.CORRECT, mgr.getQuestionState(1));
        assertEquals(QuestionState.WRONG, mgr.getQuestionState(2));
    }

    @Test
    public void testIsCorrect() {
        SessionMgr mgr = new SessionMgr("C Programming Mock Tests", db);
        Question q1 = mgr.getNextQuestion();

        int oId = 0;
        for(int i = 0; i < q1.getOptions().size(); i++) {
            if(q1.getOptions().get(i).isCorrect()) {
                oId = i;
            }
        }

        assertTrue(mgr.isCorrect(0, oId));
    }

    @Test
    public void testResetIterator() {
        SessionMgr mgr = new SessionMgr("C Programming Mock Tests", db);
        Question q1 = mgr.getNextQuestion();
        String text1 = q1.getBodyText();

        q1 = mgr.getNextQuestion();
        q1 = mgr.getNextQuestion();
        mgr.resetIterator();
        q1 = mgr.getNextQuestion();

        String text2 = q1.getBodyText();

        assertTrue(text1.equals(text2));
    }

    private Option getCorrectOption(Question question) {
        return question.getCorrectOption();
    }

    private Option getIncorrectOption(Question question) {
        for(Option op : question.getOptions()) {
            if(!op.isCorrect()) {
                return op;
            }
        }
        return null;
    }
}

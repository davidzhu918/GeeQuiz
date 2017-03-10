package edu.gatech.quiz.TaskCards;

/**
 * Created by zixiangzhu on 11/26/16.
 */
import android.support.annotation.BinderThread;

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

import android.content.Context;

import java.util.*;

/**
 * CS3300 Project 2
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class QuizMgrTest {
    // Use local DB while running unit test
    LocalQuizDB db;

    @Before
    public void setUp() {
        db = new LocalQuizDB();
        db.setLocalDatabaseFile();
    }

    @Test
    public void testGetCategories() {
        List<String> categories = db.getCategories();
        assertEquals(14, categories.size());
        assertTrue(categories.contains("Theory of Computation Mock Tests"));
        assertTrue(categories.contains("C Programming Mock Tests"));
        assertFalse(categories.contains(""));
    }

    @Test
    public void testGetSession1() {
        QuizMgr qm = new QuizMgr(db);
        SessionMgr sm;

        sm = qm.getSession("Theory of Computation Mock Tests");
        assertNotNull(sm);
        assertTrue(sm.getNumberQuestions() > 0 && sm.getNumberQuestions() <= 10);

        sm = qm.getSession("C Programming Mock Tests");
        assertNotNull(sm);
        assertTrue(sm.getNumberQuestions() > 0 && sm.getNumberQuestions() <= 10);
    }

    @Test
    public void testGetSession2() {
        QuizMgr qm = new QuizMgr(db);
        SessionMgr sm = qm.getSession("");

        assertNull(sm);
    }

    @Test
    public void testGetSession3() {
        QuizMgr qm = new QuizMgr(db);
        SessionMgr sm;

        sm = qm.createSession("Theory of Computation Mock Tests");

        Question question = sm.getNextQuestion();
        Option op = getCorrectOption(question);
        sm.setAnswer(question, op);

        assertEquals(1, sm.getNumberCorrectQuestions());

        SessionMgr sm2 = qm.getSession("Theory of Computation Mock Tests");
        assertEquals(1, sm2.getNumberCorrectQuestions());
    }

    @Test
    public void testCreateSession1() {
        QuizMgr qm = new QuizMgr(db);
        SessionMgr sm;

        sm = qm.createSession("Theory of Computation Mock Tests");
        assertNotNull(sm);
        assertTrue(sm.getNumberQuestions() > 0 && sm.getNumberQuestions() <= 10);

        sm = qm.getSession("C Programming Mock Tests");
        assertNotNull(sm);
        assertTrue(sm.getNumberQuestions() > 0 && sm.getNumberQuestions() <= 10);
    }

    @Test
    public void testCreateSession2() {
        QuizMgr qm = new QuizMgr(db);
        SessionMgr sm = qm.createSession("");

        assertNotNull(sm);
        assertTrue(sm.getNumberQuestions() == 0);
    }

    @Test
    public void testCreateSession3() {
        QuizMgr qm = new QuizMgr(db);
        SessionMgr sm;

        sm = qm.createSession("Theory of Computation Mock Tests");

        Question question = sm.getNextQuestion();
        Option op = getCorrectOption(question);
        sm.setAnswer(question, op);

        assertEquals(1, sm.getNumberCorrectQuestions());

        SessionMgr sm2 = qm.createSession("Theory of Computation Mock Tests");
        assertEquals(0, sm2.getNumberCorrectQuestions());
    }

    @Test
    public void testGetCumuScore1() {
        QuizMgr qm = new QuizMgr(db);
        SessionMgr sm;
        Question question;
        Option op;

        sm = qm.createSession("Theory of Computation Mock Tests");
        question = sm.getNextQuestion();
        op = getCorrectOption(question);
        sm.setAnswer(question, op);

        sm = qm.createSession("Theory of Computation Mock Tests");
        question = sm.getNextQuestion();
        op = getCorrectOption(question);
        sm.setAnswer(question, op);

        int[] scores = qm.getCumuScore("Theory of Computation Mock Tests");
        assertEquals(2, scores[0]);
        assertEquals(2, scores[1]);
    }

    @Test
    public void testGetCumuScore2() {
        QuizMgr qm = new QuizMgr(db);
        SessionMgr sm;
        Question question;
        Option op;

        sm = qm.createSession("Theory of Computation Mock Tests");
        question = sm.getNextQuestion();
        op = getCorrectOption(question);
        sm.setAnswer(question, op);

        sm = qm.createSession("Theory of Computation Mock Tests");
        question = sm.getNextQuestion();
        op = getIncorrectOption(question);
        sm.setAnswer(question, op);

        int[] scores = qm.getCumuScore("Theory of Computation Mock Tests");
        assertEquals(1, scores[0]);
        assertEquals(2, scores[1]);
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

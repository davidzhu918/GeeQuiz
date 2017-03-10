package edu.gatech.quiz.TaskCards;

/**
 * Created by zixiangzhu on 11/25/16.
 */

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
public class QuestionIteratorTest {
    @Before
    public void setUp() {

    }

    @Test
    public void testHasNext() {
        List<Question> l = new ArrayList<Question>();
        QuestionIterator iterator;

        iterator = new QuestionIterator(l);
        assertFalse(iterator.hasNext());

        l.add(new Question(1, null, null, null, null));
        iterator = new QuestionIterator(l);
        assertTrue(iterator.hasNext());

    }

    @Test
    public void testHasPrev() {
        List<Question> l = new ArrayList<Question>();
        QuestionIterator iterator;

        iterator = new QuestionIterator(l);
        assertFalse(iterator.hasPrevious());

        l.add(new Question(1, null, null, null, null));
        iterator = new QuestionIterator(l);
        assertFalse(iterator.hasPrevious()); //cursor did not move

    }

    @Test
    public void testNext() {
        List<Question> l = new ArrayList<Question>();
        QuestionIterator iterator;

        l.add(new Question(1, null, null, null, null));

        iterator = new QuestionIterator(l);

        Question q = iterator.next();
        assertNotNull(q);
        assertTrue(q.getId() == 1);

        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPrev() {
        List<Question> l = new ArrayList<Question>();
        QuestionIterator iterator;

        l.add(new Question(1, null, null, null, null));
        iterator = new QuestionIterator(l);
        iterator.next();
        assertFalse(iterator.hasPrevious());

        l.add(new Question(2, null, null, null, null));
        iterator = new QuestionIterator(l);
        iterator.next();
        iterator.next();
        assertTrue(iterator.hasPrevious());
        assertEquals(1, iterator.previous().getId());
        assertFalse(iterator.hasPrevious());
    }

}

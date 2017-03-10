package edu.gatech.quiz.TaskCards;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.quiz.helpers.Question;

/**
 * Created by zixiangzhu on 11/25/16.
 */

public class QuestionIterator implements QuestionIterable {
    private List<Question> qList;
    private int idx;

    private QuestionIterator() {
        idx = -1;
        qList = new ArrayList<Question>();
    }

    public QuestionIterator(List<Question> l) {
        this();
        qList.addAll(l);
    }

    public boolean hasNext() {
        return idx < qList.size() - 1;
    }

    public boolean hasPrevious() {
        return idx > 0;
    }

    public Question next() {
        return qList.get(++idx);
    }

    public Question previous() {
        return qList.get(--idx);
    }

    public void reset() {
        idx = -1;
    }
}

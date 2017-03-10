package edu.gatech.quiz.TaskCards;

import edu.gatech.quiz.helpers.Question;

/**
 * Created by zixiangzhu on 11/25/16.
 */

public interface QuestionIterable {
    public boolean hasNext();
    public Question next();

    public boolean hasPrevious();
    public Question previous();

}

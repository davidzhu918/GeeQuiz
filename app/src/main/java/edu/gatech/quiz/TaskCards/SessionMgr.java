package edu.gatech.quiz.TaskCards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import edu.gatech.quiz.data.QuizDB;
import edu.gatech.quiz.helpers.Option;
import edu.gatech.quiz.helpers.Question;
import edu.gatech.quiz.helpers.QuizSession;

/**
 * Created by zixiangzhu on 11/25/16.
 */

public class SessionMgr {
    private QuizSession session;
    private QuestionIterator iterator; //iterator for all questions, should not be exposed

    private Set<Question> wrongSet;
    private Set<Question> correctSet; //if the question was answered correctly before

    public SessionMgr(String category, QuizDB db) {
        session = QuizSession.createShortSession(category, db);
        iterator = new QuestionIterator(session.getQuestions());

        wrongSet = new HashSet<Question>();
        correctSet = new HashSet<Question>();
    }

    public boolean hasNextQuestion() {
        return iterator.hasNext();
    }

    public Question getNextQuestion() {
        return iterator.next();
    }

    public void resetIterator() {
        iterator.reset();
    }

    public boolean hasPreviousQuestion() {
        return iterator.hasPrevious();
    }

    public Question getPreviousQuestion() {
        return iterator.previous();
    }

    public boolean isCorrect(int qId, int oId) {
        Question q = session.getQuestions().get(qId);
        Option o = q.getOptions().get(oId);
        return o.isCorrect();
    }

    public QuestionState getQuestionState(int qId) {
        Question q = session.getQuestions().get(qId);
        if(correctSet.contains(q))
            return QuestionState.CORRECT;
        else if(wrongSet.contains(q))
            return QuestionState.WRONG;
        else
            return QuestionState.BLANK;
    }

    public void setAnswer(int qId, int oId) {
        Question q = session.getQuestions().get(qId);
        Option o = q.getOptions().get(oId);
        setAnswer(q, o);
    }

    public void setAnswer(Question q, Option o) {
        session.setUserAnswer(q, o);
        if(o.isCorrect()) {
            correctSet.add(q);
            wrongSet.remove(q);
        } else {
            wrongSet.add(q);
            correctSet.remove(q);
        }
    }

    public boolean correctBefore(Question q) {
        return correctSet.contains(q);
    }

    public int getQuestionIndex(Question q) {
        for(int i = 0; i < session.getQuestions().size(); i++) {
            if(q.equals(session.getQuestions().get(i)))
                return i;
        }
        return -1;
    }

    public int getNumberQuestions() {
        return session.getQuestions().size();
    }

    public int getNumberCorrectQuestions() {
        return session.getScore();
    }

    public int getNumberAnsweredQuestion() {
        return correctSet.size() + wrongSet.size();
    }

    /**
     * order wrong questions by their relative positions in the question list
     * @return
     */
    public QuestionIterable getIteratorWrong() {
        List<Question> qList = new ArrayList<Question>();
        for(Question q : wrongSet) {
            qList.add(q);
        }
        Collections.sort(qList, new Comparator<Question>() {
            @Override
            public int compare(Question q1, Question q2) {
                return getQuestionIndex(q1) - getQuestionIndex(q2);
            }
        });
        return new QuestionIterator(qList);
    }

    public Option getUserAnswer(Question q) {
        return session.getUserAnswer(q);
    }

}

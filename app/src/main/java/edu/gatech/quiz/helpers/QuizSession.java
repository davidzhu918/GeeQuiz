package edu.gatech.quiz.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import edu.gatech.quiz.data.QuizDB;

/**
 * CS3300 Project 2
 */
public class QuizSession {
    // Copy your Deliverable 1 Implementation Here
    static final int SHORTSIZE = 10;

    final private List<Question> questions;
    final private Map<Question, Option> userAnswers;

    private QuizSession() {
        questions = new ArrayList<Question>();
        userAnswers = new HashMap<Question, Option>();
    }

    //initialize the QuizSession with an existing list
    private QuizSession(List<Question> questions) {
        this();
        this.questions.addAll(questions);
    }

    public static QuizSession createShortSession(String category, QuizDB db) {
        List<Question> allQuestions = db.getCategoryQuestions(category);
        if(allQuestions == null)
            return new QuizSession();

        Random r = new Random();
        int randInt = 0;

        int numQuestions = SHORTSIZE;
        if(allQuestions.size() < SHORTSIZE) numQuestions = allQuestions.size();

        List<Question> shortQuestions = new ArrayList<Question>();
        for(int i = 0; i < numQuestions; i++) {
            //randomly grab a question
            randInt = r.nextInt(allQuestions.size());
            Question q = allQuestions.get(randInt);
            shortQuestions.add(q);

            //remove this question from allQuestions so we don't grab it a second time
            allQuestions.remove(randInt);
        }

        return new QuizSession(shortQuestions);
    }

    public static QuizSession createLongSession(String category, QuizDB db) {
        List<Question> allQuestions = db.getCategoryQuestions(category);
        if(allQuestions == null)
            return new QuizSession();

        return new QuizSession(allQuestions);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setUserAnswer(Question q, Option o) {
        userAnswers.put(q, o);
    }

    public Option getUserAnswer(Question q) { return userAnswers.get(q); }

    public boolean solvedAll() {
        for(int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            if(!userAnswers.containsKey(q)) return false;
        }
        return true;
    }

    /**
     * returns the number of correct answers
     * blank answers are not counted
     * @return score
     */
    public int getScore() {
        int score = 0;
        for(Question q : userAnswers.keySet()) {
            if(userAnswers.get(q).isCorrect())
                score++;
        }
        return score;
    }
}

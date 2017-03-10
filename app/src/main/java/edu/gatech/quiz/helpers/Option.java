package edu.gatech.quiz.helpers;

import java.io.Serializable;

/**
 * CS3300 Project 2
 */
public class Option implements Serializable{
    final int id;
    final private String optionText;
    final private boolean correct;

    public Option(int id, String optionText, boolean correct) {
        this.id = id;
        this.optionText = optionText;
        this.correct = correct;
    }

    public int getId() { return id; }

    public String getOptionText() {
        return optionText;
    }

    public boolean isCorrect() {
        return correct;
    }
}
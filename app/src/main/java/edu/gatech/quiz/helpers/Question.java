package edu.gatech.quiz.helpers;

import java.io.Serializable;
import java.util.List;

/**
 * CS3300 Project 2
 */
public class Question implements Serializable {
    final private int id;
    final private String category;
    final private String bodyText;
    final private List<Option> options;
    final private String explanation;

    public Question(int id, String category, String bodyText, List<Option> options,
            String explanation) {
        this.id = id;
        this.category = category;
        this.bodyText = bodyText;
        this.options = options;
        this.explanation = explanation;
    }

    public int getId() { return id; }

    public String getCategory() {
        return category;
    }

    public String getBodyText() {
        return bodyText;
    }

    public List<Option> getOptions() {
        return options;
    }

    public String getExplanation() {
        return explanation;
    }

    public Option getCorrectOption() {
        for(int i = 0; i < options.size(); i++) {
            Option o = options.get(i);
            if(o.isCorrect()) return o;
        }
        return null;
    }

    @Override
    public int hashCode() {
        return 17 * id + 23 * category.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Question) {
            Question e = (Question)o;
            return e.id == this.id && this.category.equals(e.category);
        }
        return false;
    }
}

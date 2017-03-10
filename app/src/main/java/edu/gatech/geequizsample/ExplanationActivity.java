package edu.gatech.geequizsample;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.gatech.quiz.TaskCards.QuestionIterable;
import edu.gatech.quiz.TaskCards.QuizMgr;
import edu.gatech.quiz.TaskCards.SessionMgr;
import edu.gatech.quiz.helpers.Question;
import edu.gatech.quiz.helpers.Option;

public class ExplanationActivity extends Activity implements View.OnClickListener {
    private QuestionIterable iterator;
    private SessionMgr sm;
    private Question q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);

        Intent intent = getIntent();
        String selectedCategory = intent.getStringExtra(QuizSessionActivity.EXTRA_MESSAGE);
        sm = QuizMgr.getInstance(getApplicationContext()).getSession(selectedCategory);
        iterator = sm.getIteratorWrong();

        TextView summaryTextView = (TextView) findViewById(R.id.sessionSummary);
        int attempts = sm.getNumberAnsweredQuestion();
        int total = sm.getNumberQuestions();
        int corrects = sm.getNumberCorrectQuestions();
        summaryTextView.setText("You answered " + attempts + " / " + total
                + " questions: " + corrects + " correct; " + (attempts - corrects) + " incorrect");

        if(iterator.hasNext()) {
            q = iterator.next();
            setContents(q, sm.getUserAnswer(q));

            //register action listeners
            Button next = (Button) findViewById(R.id.nextButton);
            next.setOnClickListener(this);

            Button prev = (Button) findViewById(R.id.prevButton);
            prev.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                if(iterator.hasNext()) {
                    q = iterator.next();
                    setContents(q, sm.getUserAnswer(q));
                }
                break;
            case R.id.prevButton:
                if(iterator.hasPrevious()) {
                    q = iterator.previous();
                    setContents(q, sm.getUserAnswer(q));
                }
                break;
            default:
                System.err.println("Click on Unhandled Widget");
        }

    }

    private void setContents(Question q, Option user_option) {
        TextView questionTextView = (TextView) findViewById(R.id.explanation_question_text);
        TextView wrongTextView = (TextView) findViewById(R.id.explanation_wrong_text);
        TextView correctTextView = (TextView) findViewById(R.id.explanation_correct_text);
        TextView explanationTextView = (TextView) findViewById(R.id.explanation_text);

        questionTextView.setText("Question " + (sm.getQuestionIndex(q) + 1) + "\n\n" + q.getBodyText());
        wrongTextView.setText("Your answer: " + user_option.getOptionText());
        correctTextView.setText("Correct answer: " + q.getCorrectOption().getOptionText());
        explanationTextView.setText(q.getExplanation());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_explanation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.explanation_finish:
                startActivity(new Intent(this, CategorySelectActivity.class));
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

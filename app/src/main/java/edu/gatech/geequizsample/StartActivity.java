package edu.gatech.geequizsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import edu.gatech.quiz.TaskCards.QuizMgr;
import edu.gatech.quiz.data.QuizDB;


public class StartActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //initialize quizmgr
        QuizMgr.getInstance(getApplicationContext());

        Button takeQuiz = (Button) findViewById(R.id.take_quiz);
        takeQuiz.setOnClickListener(this);

        Button dashboard = (Button) findViewById(R.id.dashboard);
        dashboard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.take_quiz:
                startActivity(new Intent(this, CategorySelectActivity.class));
                break;
            case R.id.dashboard:
                startActivity(new Intent(this, DashboardActivity.class));
                break;
            default:
                System.err.println("Click on Unhandled Widget");
        }

    }
}

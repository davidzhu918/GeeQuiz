package edu.gatech.geequizsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.geequizsample.R;
import edu.gatech.quiz.TaskCards.QuestionIterable;
import edu.gatech.quiz.TaskCards.QuizMgr;
import edu.gatech.quiz.TaskCards.SessionMgr;

public class DashboardActivity extends Activity {
    QuizMgr qm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        qm = QuizMgr.getInstance(getApplicationContext());

        final ListView listview = (ListView) findViewById(R.id.categories);

        // TODO: Get the list of categories here instead of using fake dashboardEntries
        final List<String[]> dashboardEntries = new ArrayList<>();
        for(String s : qm.getCategories()) {
            SessionMgr sm = qm.getSession(s);
            int currCorrect = sm.getNumberCorrectQuestions();
            int currTotal = sm.getNumberAnsweredQuestion();

            int[] cumuScore = qm.getCumuScore(s);
            int cumuCorrect = cumuScore[0];
            int cumuTotal = cumuScore[1];

            String message = currCorrect + "/" + currTotal + "\t\t\tCumulative: " + cumuCorrect + "/" + cumuTotal;

            dashboardEntries.add(new String[] {s, message});
        }

        // Note: the last two parameters are dummy params
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, dashboardEntries) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                String[] entry = dashboardEntries.get(position);
                text1.setText(entry[0]);
                text2.setText(entry[1]);
                text2.setContentDescription("c" + position + "_score");
                return view;
            }
        };

        listview.setAdapter(adapter);
    }

}

package edu.gatech.geequizsample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.gatech.quiz.TaskCards.QuizMgr;
import edu.gatech.quiz.TaskCards.SessionMgr;


public class CategorySelectActivity extends Activity implements AdapterView.OnItemClickListener {
    public final static String EXTRA_MESSAGE = "edu.gatech.qeequizsample.MESSAGE";
    public final static String EXTRA_FLAG = "edu.gatech.qeequizsample.FLAG";

    QuizMgr qm;

    private class CategoryAdapter extends ArrayAdapter<String> {
        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public CategoryAdapter(Context context, int textViewResourceId,
                               List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        final ListView listview = (ListView) findViewById(R.id.categories);

        qm = QuizMgr.getInstance(getApplicationContext());

        // TODO: Get the list of categories here instead of using fakeCategories
        List<String> categories = qm.getCategories();

        final CategoryAdapter adapter = new CategoryAdapter(this, android.R.layout.simple_list_item_1, categories);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Intent i = new Intent(this, QuizSessionActivity.class);
        final ListView listview = (ListView) findViewById(R.id.categories);
        final String selectedCategory =(String) (listview.getItemAtPosition(position));
        i.putExtra(EXTRA_MESSAGE, selectedCategory);

        //get user option
        AlertDialog.Builder builder1 = new AlertDialog.Builder(CategorySelectActivity.this);
        builder1.setMessage("Start a new session?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        i.putExtra(EXTRA_FLAG, true);
                        System.out.println("Yes");
                        startActivity(i);
                        finish();

                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        i.putExtra(EXTRA_FLAG, false);
                        System.out.println("No");
                        startActivity(i);
                        finish();

                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        SessionMgr sm = qm.getSession(selectedCategory);
        if(sm.getNumberAnsweredQuestion() != 0) {
            alert11.show();
        } else {
            System.out.println("default");
            i.putExtra(EXTRA_FLAG, false);
            startActivity(i);
            finish();
        }
    }

}

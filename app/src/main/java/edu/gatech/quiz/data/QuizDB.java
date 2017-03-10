package edu.gatech.quiz.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.gatech.quiz.helpers.Option;
import edu.gatech.quiz.helpers.Question;

/**
 * CS3300 Project 2
 * Provides getCategories()
 */
public class QuizDB {

    protected SQLiteDatabase db;
    QuizDBHelper helper;

    public QuizDB(){
        // default constructor for FakeQuizDB
    }

    public QuizDB(Context context) {
        helper = new QuizDBHelper(context);
        try {
            helper.createDataBase();
            db = helper.getDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getCategories() {
        List<String> categories = new ArrayList<String>();
	// TODO: Adapt logic to Android SQLDatabase class
	// Instead of SQL Connection & Statement, Use db.rawQuery or db.query
	// Use Cursor class to process data
        try {
            String query = "SELECT * from categories";
            Cursor cursor = db.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String cat_id = cursor.getString(cursor.getColumnIndex("id"));
                String cat_title = cursor.getString(cursor.getColumnIndex("title"));
                categories.add(cat_title);
                cursor.moveToNext();
            }

        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        return categories.size() > 0 ? categories : null;
    }

    public List<Question> getCategoryQuestions(String category)
    {
        List<Question> questions = new ArrayList<Question>();
	// TODO: Adapt DB logic to Android SQLDatabase class (See above)
        //this SQL gets all the questions for the category given in the parameter

        try {
            String sql = "SELECT * from questions JOIN categories ON " +
                    "questions.category_id = categories.id WHERE categories.title = ?";

            Cursor cursor = db.rawQuery(sql, new String[]{category});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int question_id = cursor.getInt(0); //first column is question id
                String sub_sql = "SELECT * FROM answers WHERE question_id = " + question_id;
                Cursor sub_cursor = db.rawQuery(sub_sql, null);
                List<Option> options = new ArrayList<Option>();

                sub_cursor.moveToFirst();
                while(!sub_cursor.isAfterLast()) {
                    int opId = sub_cursor.getInt(sub_cursor.getColumnIndex("id"));
                    boolean opCorrect = sub_cursor.getInt(sub_cursor.getColumnIndex("correct")) > 0;
                    String opText = sub_cursor.getString(sub_cursor.getColumnIndex("body"));

                    options.add(new Option(opId, opText, opCorrect));

                    sub_cursor.moveToNext();
                }

                String qBody = cursor.getString(cursor.getColumnIndex("body"));
                String qExp = cursor.getString(cursor.getColumnIndex("explanation"));

                questions.add(new Question(question_id, category, qBody, options, qExp));

                cursor.moveToNext();
            }

        } catch(Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return questions.size() > 0 ? questions : null;
    }

}

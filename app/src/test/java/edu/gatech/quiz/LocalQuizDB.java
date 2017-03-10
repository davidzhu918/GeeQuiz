package edu.gatech.quiz;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;

import edu.gatech.quiz.data.QuizDB;

/**
 * Created by shauvik on 7/14/15.
 */
public class LocalQuizDB extends QuizDB {

    public void setLocalDatabaseFile() {
        String path = LocalQuizDB.class.getResource("/quiz.db").getPath();
        System.out.println(path);
        File dbFile = new File(path);
        if(dbFile.exists()) {
            String dbPath = dbFile.getAbsolutePath();
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
        }
    }
}

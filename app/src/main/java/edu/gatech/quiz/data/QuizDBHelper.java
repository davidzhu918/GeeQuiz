package edu.gatech.quiz.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class QuizDBHelper extends SQLiteOpenHelper {

    //The Android's default system path of the application database.
    // change edu.gatech.geequizsample with YOUR_PACKAGE_NAME
    private static String DB_PATH = "/data/data/edu.gatech.geequizsample/databases/";
    public static final String DB_NAME = "quiz.db";
//    private SQLiteDatabase db;
    private final Context context;

    public QuizDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    public void createDataBase() throws IOException{
        boolean dbExist = checkDataBase();
        if(!dbExist){
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database to system");
            }
        }
    }

    private boolean checkDataBase(){
        File dbFile = context.getDatabasePath(DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException{
        //Open your local db as the input stream
        InputStream is = context.getAssets().open("databases/"+DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream os = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer))>0){
            os.write(buffer, 0, length);
        }

        //Close the streams
        os.flush();
        os.close();
        is.close();
    }

    public SQLiteDatabase getDataBase() throws SQLException {
        return this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

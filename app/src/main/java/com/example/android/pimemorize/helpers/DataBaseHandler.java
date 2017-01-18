package com.example.android.pimemorize.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.pimemorize.HighScore;

import java.util.ArrayList;

public class DataBaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "scoresManager";
    private static final String TABLE_SCORES = "scores";
    private static final String KEY_ID = "id";
    private static final String KEY_SCORE = "score";
    private static final String KEY_DATE = "date";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HIGH_SCORES_TABLE = "CREATE TABLE " + TABLE_SCORES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SCORE + " INTEGER,"
                + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_HIGH_SCORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);
    }

    public void addHighScore(HighScore highScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SCORE, highScore.getScore());
        values.put(KEY_DATE, highScore.getDate());

        db.insert(TABLE_SCORES, null, values);
        db.close();
    }

    public ArrayList<HighScore> getAllHighScoresByScore() {
        String query = "SELECT * FROM " + TABLE_SCORES + " ORDER BY " + KEY_SCORE + " DESC";
        return getAllHighScores(query);
    }

    public ArrayList<HighScore> getAllHighScoresByDate() {
        String query = "SELECT * FROM " + TABLE_SCORES + " ORDER BY " + "date(" + KEY_DATE + ")" + " DESC";
        return getAllHighScores(query);
    }

    public ArrayList<HighScore> getAllHighScores(String query) {
        ArrayList<HighScore> highScores = new ArrayList<HighScore>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                HighScore highScore = new HighScore();
                highScore.setId(Integer.parseInt(cursor.getString(0)));
                highScore.setScore(Integer.parseInt(cursor.getString(1)));
                highScore.setDate(cursor.getString(2));

                highScores.add(highScore);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return highScores;
    }

}

package com.mxymin.android.pipal.models;

public class HighScore {
    int id;
    int score;
    String date;

    public HighScore() {
        // empty constructor
    };

    public HighScore(int id, int score, String date) {
        this.id = id;
        this.score = score;
        this.date = date;
    }

    public HighScore(int score, String date) {
        this.score = score;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

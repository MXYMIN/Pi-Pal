package com.mxymin.android.pipal.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mxymin.android.pipal.models.HighScore;
import com.mxymin.android.pipal.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ScoresAdapter extends ArrayAdapter<HighScore> {
    public ScoresAdapter(Context context, ArrayList<HighScore> highScores) {
        super(context, 0, highScores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HighScore highScore = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.scores_list_item, parent, false);
        }

        TextView digitsTextView = (TextView) convertView.findViewById(R.id.scores_list_item_digits);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.scores_list_item_date);

        digitsTextView.setText(String.valueOf(highScore.getScore()));

        DateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try{
            date = oldFormat.parse(highScore.getDate());
        } catch (ParseException e) {
            Log.e("ScoresAdapter", "Cannot parse date: " + highScore.getDate());
        }
        DateFormat newFormat = DateFormat.getDateInstance();
        String dateString = newFormat.format(date);
        dateTextView.setText(dateString);

        return convertView;
    }
}

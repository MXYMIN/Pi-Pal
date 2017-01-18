package com.example.android.pimemorize.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.pimemorize.models.HighScore;
import com.example.android.pimemorize.R;

import java.util.ArrayList;

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
        dateTextView.setText(highScore.getDate());

        return convertView;
    }
}

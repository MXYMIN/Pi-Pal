package com.example.android.pimemorize.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.android.pimemorize.HighScore;
import com.example.android.pimemorize.R;
import com.example.android.pimemorize.adapters.ScoresAdapter;
import com.example.android.pimemorize.helpers.DataBaseHandler;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ScoresActivity extends AppCompatActivity {
    ArrayList<HighScore> highScores;
    boolean sortedByScore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final DataBaseHandler db = new DataBaseHandler(this);
        highScores = db.getAllHighScoresByScore();
        final ScoresAdapter adapter = new ScoresAdapter(this, highScores);
        ListView scoresListView = (ListView) findViewById(R.id.scores_list_view);
        scoresListView.setEmptyView(findViewById(android.R.id.empty));
        scoresListView.setAdapter(adapter);

        final ToggleButton digitsHeader = (ToggleButton) findViewById(R.id.scores_list_header_digits);
        final ToggleButton dateHeader = (ToggleButton) findViewById(R.id.scores_list_header_date);

        digitsHeader.setChecked(true);
        digitsHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                digitsHeader.setChecked(true);
                dateHeader.setChecked(false);
                if (!sortedByScore) {
                    // Can't point highScores to a new arraylist, so clear it and add contents from query
                    highScores.clear();
                    highScores.addAll(db.getAllHighScoresByScore());
                    sortedByScore = true;
                    adapter.notifyDataSetChanged();
                }
            }
        });
        dateHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateHeader.setChecked(true);
                digitsHeader.setChecked(false);
                if (sortedByScore) {
                    highScores.clear();
                    highScores.addAll(db.getAllHighScoresByDate());
                    sortedByScore = false;
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}

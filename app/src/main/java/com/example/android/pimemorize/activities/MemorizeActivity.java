package com.example.android.pimemorize.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.android.pimemorize.Constants;
import com.example.android.pimemorize.R;
import com.example.android.pimemorize.adapters.PiMemorizeAdapter;
import com.example.android.pimemorize.helpers.StringHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class MemorizeActivity extends AppCompatActivity {

    private String mPi;
    private ArrayList<String> mPiDigitsArrayList;
    private int mDigitsPerRow;
    private SharedPreferences mSharedPrefs;
    private PiMemorizeAdapter mAdapter;
    private ListView mPiListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorize);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mPiListView = (ListView) findViewById(R.id.pi_memorize_list_view);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Read pi from file and remove decimal for easier string groupings
        mPi = StringHelper.readFromFile(this, "pi.txt");
        mPi = mPi.replace(".", "");

        initializePiList();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // If digits per row setting was modified, reinitialize the pi list
        if (Integer.parseInt(mSharedPrefs.getString(getResources().getString(R.string.pref_key_digits_per_row), Constants.DEFAULT_DIGITS_PER_ROW)) != mDigitsPerRow) {
            initializePiList();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, UserPreferencesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializePiList() {
        // Update digits per row to be displayed
        mDigitsPerRow = Integer.parseInt(mSharedPrefs.getString(getResources().getString(R.string.pref_key_digits_per_row), Constants.DEFAULT_DIGITS_PER_ROW));

        // Get pi and store digits in groups of x digits in an array list depending on user settings
        mPiDigitsArrayList = new ArrayList<String>(Arrays.asList(StringHelper.splitStringEvery(mPi, mDigitsPerRow)));

        // Initialize the adapter and listview
        mAdapter = new PiMemorizeAdapter(this, mPiDigitsArrayList);
        mPiListView.setAdapter(mAdapter);
    }
}

package com.example.android.pimemorize.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.pimemorize.Constants;
import com.example.android.pimemorize.R;
import com.example.android.pimemorize.adapters.PiGameAdapter;
import com.example.android.pimemorize.fragments.GameOverDialog;
import com.example.android.pimemorize.fragments.NumPadFragment;
import com.example.android.pimemorize.helpers.StringHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class PracticeActivity extends AppCompatActivity implements NumPadFragment.OnNumberClickedListener{

    private EditText mGoToEditText;
    private ListView mPiListView;
    private NumPadFragment mNumPadFrag;
    private SharedPreferences mSharedPrefs;
    private String mPi;
    private int mDigitsPerRow;
    private String mNewRowString;
    private ArrayList<String> mPiDigitsArrayList;
    private ArrayList<String> mUserPiArrayList;
    private PiGameAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Get references to UI elements
        View listHeaderView = findViewById(R.id.list_goto_header);
        mGoToEditText = (EditText) listHeaderView.findViewById(R.id.go_to_edit_text);
        ImageButton goToRowButton = (ImageButton) listHeaderView.findViewById(R.id.go_to_row_button);

        mPiListView = (ListView) findViewById(R.id.pi_practice_list_view);

        mNumPadFrag = (NumPadFragment) getSupportFragmentManager().findFragmentById(R.id.num_pad);

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

    private void updateListItem(int index, String str) {
        mUserPiArrayList.set(index, str);
        mAdapter.notifyDataSetChanged();
    }

    private void initializePiList() {
        // Update digits per row to be displayed
        mDigitsPerRow = Integer.parseInt(mSharedPrefs.getString(getResources().getString(R.string.pref_key_digits_per_row), Constants.DEFAULT_DIGITS_PER_ROW));
        mNewRowString = StringHelper.generateMaskedRowString("", mDigitsPerRow);

        // Get pi and store digits in groups of x digits in an array list depending on user settings
        mPiDigitsArrayList = new ArrayList<String>(Arrays.asList(StringHelper.splitStringEvery(mPi, mDigitsPerRow)));

        // Initialize the user's pi array list
        mUserPiArrayList = new ArrayList<String>();
        // Add first masked row with the decimal ex: ?.? ? ?
        mUserPiArrayList.add(mNewRowString.replaceFirst(Pattern.quote(" "), "."));

        // Initialize the adapter and listview
        mAdapter = new PiGameAdapter(this, mUserPiArrayList);
        mPiListView.setAdapter(mAdapter);
    }

    @Override
    public void onNumberClicked(String rowString) {

        // Reset flag for error row to declare no error
        // Removes error display on row after user presses a button
        mAdapter.setInvalidRow(false);

        // Smoothly scroll to bottom of list
        mPiListView.smoothScrollToPosition(mAdapter.getCount());

        // String that is being displayed with trailing '?'s
        String displayRowString = rowString;

        // Generate the masked string with spaces in between
        displayRowString = StringHelper.generateMaskedRowString(displayRowString, mDigitsPerRow);

        // Add the decimal point for the first line '3.141'
        if (mAdapter.getCount() == 1) {
            displayRowString = displayRowString.replaceFirst(" ", ".");
        }

        // Update list item visually after user's input
        updateListItem(mAdapter.getCount() - 1, displayRowString);

        // Validate user's input when current row is filled
        if (rowString.length() == mDigitsPerRow) {
            // Compare user's input with correct digits of pi
            if (rowString.equals(mPiDigitsArrayList.get(mAdapter.getCount() - 1))) {
                // Add a new placeholder row and smoothly scroll to last list item
                mAdapter.add(mNewRowString);
                mPiListView.smoothScrollToPosition(mAdapter.getCount());
            }
            else {
                // Show error in row
                mAdapter.setInvalidRow(true);
                updateListItem(mAdapter.getCount() - 1, displayRowString);

                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.practice_relative_layout);
                // Show snackbar with correct row digits
                Snackbar snackbar = Snackbar
                        .make(relativeLayout, (mPiDigitsArrayList.get(mAdapter.getCount() - 1).toString()), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }
}

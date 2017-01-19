package com.mxymin.android.pipal.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mxymin.android.pipal.Constants;
import com.mxymin.android.pipal.models.HighScore;
import com.mxymin.android.pipal.fragments.GameOverDialog;
import com.mxymin.android.pipal.fragments.NumPadFragment;
import com.mxymin.android.pipal.adapters.PiGameAdapter;
import com.mxymin.android.pipal.R;
import com.mxymin.android.pipal.helpers.DataBaseHandler;
import com.mxymin.android.pipal.helpers.StringHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Pattern;

public class GameActivity extends AppCompatActivity implements NumPadFragment.OnNumberClickedListener, GameOverDialog.GameOverDialogListener {

    private PiGameAdapter mAdapter;
    private ListView mPiListView;
    private ArrayList<String> mUserPiArrayList;
    private ArrayList<String> mPiDigitsArrayList;
    private TextView mCurrentRowTextView;
    private TextView mDigitsCorrectTextView;
    private int mDigitsPerRow;
    private int mDigitsCorrect;
    private String mNewRowString;
    private String mPi;
    private SharedPreferences mSharedPrefs;
    private NumPadFragment mNumPadFrag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Get references to UI elements
        View mHeaderLayout = findViewById(R.id.top_list_game_header);
        mCurrentRowTextView = (TextView) mHeaderLayout.findViewById(R.id.current_row_text_view);
        mDigitsCorrectTextView = (TextView) mHeaderLayout.findViewById(R.id.digits_correct_text_view);
        mPiListView = (ListView) findViewById(R.id.pi_game_list_view);

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

    private void updateListItem(int index, String str) {
        mUserPiArrayList.set(index, str);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_game_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, UserPreferencesActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_refresh:
                initializePiList();
                mNumPadFrag.clearRowString();
                mNumPadFrag.enableNumPad();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onNumberClicked(String currentRow) {
        // Reset flag for error row to declare no error
        // Removes error display on row after user presses a button
        mAdapter.setInvalidRow(false);
        mPiListView.smoothScrollToPosition(mAdapter.getCount());
        String displayRowString = getDisplayRowString(currentRow);
        updateListItem(mAdapter.getCount() - 1, displayRowString);
        // Validate user's input when current row is filled
        if (currentRow.length() == mDigitsPerRow) {
            validateRow(currentRow, displayRowString);
        }
    }

    private void initializePiList() {
        // Update digits per row to be displayed
        mDigitsPerRow = Integer.parseInt(mSharedPrefs.getString(getResources().getString(R.string.pref_key_digits_per_row), Constants.DEFAULT_DIGITS_PER_ROW));
        mNewRowString = StringHelper.generateMaskedRowString("", mDigitsPerRow);
        mDigitsCorrect = 0;

        // Get pi and store digits in groups of x digits in an array list depending on user settings
        mPiDigitsArrayList = new ArrayList<String>(Arrays.asList(StringHelper.splitStringEvery(mPi, mDigitsPerRow)));

        mUserPiArrayList = new ArrayList<String>();
        mUserPiArrayList.add(mNewRowString.replaceFirst(Pattern.quote(" "), "."));

        mAdapter = new PiGameAdapter(this, mUserPiArrayList);
        mPiListView.setAdapter(mAdapter);
    }

    private String getDisplayRowString(String currentRow) {
        String displayRowString = currentRow;
        displayRowString = StringHelper.generateMaskedRowString(displayRowString, mDigitsPerRow);

        // Add the decimal point for the first line '3.141'
        if (mAdapter.getCount() == 1) {
            displayRowString = displayRowString.replaceFirst(" ", ".");
        }

        return displayRowString;
    }

    private void validateRow(String currentRow, String displayRowString) {
        if (currentRow.equals(mPiDigitsArrayList.get(mAdapter.getCount() - 1))) {
            // Add a new placeholder row and smoothly scroll to last list item
            mAdapter.add(mNewRowString);
            mPiListView.smoothScrollToPosition(mAdapter.getCount());
            mDigitsCorrect += mDigitsPerRow;
            mDigitsCorrectTextView.setText(String.format(getString(R.string.digits_correct_d), mDigitsCorrect));
            mCurrentRowTextView.setText(String.format(getString(R.string.current_row_d), (mAdapter.getCount())));
        }
        else {
            updateFinalScore(currentRow);
            mAdapter.setInvalidRow(true);
            updateListItem(mAdapter.getCount() - 1, displayRowString);

            mNumPadFrag.disableNumPad();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c.getTime());

            DataBaseHandler db = new DataBaseHandler(this);
            db.addHighScore(new HighScore(mDigitsCorrect, formattedDate));

            // Open up game over dialog after 500ms delay
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    GameOverDialog dialog = GameOverDialog.newInstance(mDigitsCorrect, mAdapter.getCount());
                    dialog.show(getFragmentManager(), "GameOverDialog");
                }
            }, 500);
        }
    }

    private void updateFinalScore(String currentRow) {
        int bonus = 0;
        for (int i = 0; i < mDigitsPerRow; i++) {
            if (currentRow.charAt(i) == mPiDigitsArrayList.get(mAdapter.getCount() - 1).charAt(i)) {
                bonus++;
            } else {
                break;
            }
        }
        mDigitsCorrect += bonus;
    }

    @Override
    public void onRetryClick() {
        initializePiList();
        mNumPadFrag.clearRowString();
        mNumPadFrag.enableNumPad();
    }
}

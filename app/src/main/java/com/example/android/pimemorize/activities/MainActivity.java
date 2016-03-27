package com.example.android.pimemorize.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.pimemorize.Constants;
import com.example.android.pimemorize.fragments.GameOverDialog;
import com.example.android.pimemorize.fragments.NumPadFragment;
import com.example.android.pimemorize.adapters.PiAdapter;
import com.example.android.pimemorize.R;
import com.example.android.pimemorize.helpers.StringHelper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements NumPadFragment.OnNumberClickedListener, GameOverDialog.GameOverDialogListener {

    private String LOG_TAG = MainActivity.class.getSimpleName();
    private PiAdapter mAdapter;
    private ListView mPiListView;
    private ArrayList<String> mUserPiArrayList;
    private ArrayList<String> mPiDigitsArrayList;
    private TextView mCurrentRowTextView;
    private TextView mDigitsCorrectTextView;
    private int mDigitsPerRow;
    private String mNewRowString;
    private String mPi;
    private SharedPreferences mSharedPrefs;
    private NumPadFragment mNumPadFrag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Get references to UI elements
        View mHeaderLayout = findViewById(R.id.top_list_header);
        mCurrentRowTextView = (TextView) mHeaderLayout.findViewById(R.id.current_row_text_view);
        mDigitsCorrectTextView = (TextView) mHeaderLayout.findViewById(R.id.digits_correct_text_view);
        mPiListView = (ListView) findViewById(R.id.pi_list_view);

        mNumPadFrag = (NumPadFragment) getSupportFragmentManager().findFragmentById(R.id.num_pad);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Read pi from file and remove decimal for easier string groupings
        mPi = readPiFromFile(this);
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

    private String readPiFromFile(Context context) {

        String ret = "";

        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("pi.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void updateListItem(int index, String str) {
        mUserPiArrayList.set(index, str);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
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
    public void onNumberClicked(String rowString) {

        final int currentRow = mAdapter.getCount();
        final int digitsCorrect = (mAdapter.getCount() - 1) * mDigitsPerRow;

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
                mDigitsCorrectTextView.setText("digits correct: " + (digitsCorrect + mDigitsPerRow));
                mCurrentRowTextView.setText("current row: " + (currentRow + 1));
            }
            else {
                // Show error in row
                mAdapter.setInvalidRow(true);
                updateListItem(mAdapter.getCount() - 1, displayRowString);

                // Disable num pad
                mNumPadFrag.disableNumPad();

                // Open up game over dialog after 500ms delay
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GameOverDialog dialog = GameOverDialog.newInstance(digitsCorrect, currentRow);
                        dialog.show(getFragmentManager(), "GameOverDialog");
                    }
                }, 500);
            }
        }
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
        mAdapter = new PiAdapter(this, mUserPiArrayList);
        mPiListView.setAdapter(mAdapter);
    }

    @Override
    public void onRetryClick() {
        initializePiList();
        mNumPadFrag.clearRowString();
        mNumPadFrag.enableNumPad();
    }
}

package com.example.android.pimemorize.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pimemorize.Constants;
import com.example.android.pimemorize.R;
import com.example.android.pimemorize.adapters.PiMemorizeAdapter;
import com.example.android.pimemorize.helpers.StringHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class MemorizeActivity extends AppCompatActivity {

    private String mPi;
    private int mDigitsPerRow;
    private SharedPreferences mSharedPrefs;
    private ListView mPiListView;
    private PiMemorizeAdapter mAdapter;
    private EditText mGoToEditText;

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

        View listHeaderView = findViewById(R.id.list_goto_header);
        mGoToEditText = (EditText) listHeaderView.findViewById(R.id.go_to_edit_text);
        mPiListView = (ListView) findViewById(R.id.pi_memorize_list_view);
        ImageButton goToRowButton = (ImageButton) listHeaderView.findViewById(R.id.go_to_row_button);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Read pi from file and remove decimal for easier string groupings
        mPi = StringHelper.readFromFile(this, "pi.txt");
        mPi = mPi.replace(".", "");

        initializePiList();

        // Change number of visible list items depending on edit text focus
        mGoToEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mAdapter.setNumberOfVisibleItems(3);
                } else {
                    hideKeyboard();
                    mAdapter.setNumberOfVisibleItems(6);
                }
            }
        });

        // Process edit text input on image button click
        goToRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToInputtedRow();
            }
        });

        // Alternatively, user can press done on keyboard to process input
        mGoToEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mGoToEditText.clearFocus();
                    goToInputtedRow();
                }
                return false;
            }
        });
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
        ArrayList<String> piDigitsArrayList = new ArrayList<String>(Arrays.asList(StringHelper.splitStringEvery(mPi, mDigitsPerRow)));

        // Initialize the adapter and listview
        mAdapter = new PiMemorizeAdapter(this, piDigitsArrayList);
        mPiListView.setAdapter(mAdapter);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mGoToEditText.getWindowToken(), 0);
    }

    // Remove edit text focus when background touched
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    hideKeyboard();
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    // Set listview to display row based on user input
    // Visually update entered listview item row by notifying adapter
    private void goToInputtedRow() {
        if (mGoToEditText.getText() != null && !mGoToEditText.getText().toString().isEmpty()) {
            int listPosition = Integer.parseInt(mGoToEditText.getText().toString()) - 1;

            if (listPosition <= mAdapter.getCount()) {
                mAdapter.setSelectedListItem(listPosition);
                mAdapter.notifyDataSetChanged();
                mPiListView.setSelection(listPosition);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), String.format(getString(R.string.only_d_rows_available_message), mAdapter.getCount()), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}

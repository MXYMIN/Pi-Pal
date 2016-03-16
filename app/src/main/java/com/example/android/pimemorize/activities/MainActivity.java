package com.example.android.pimemorize.activities;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.android.pimemorize.Constants;
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

public class MainActivity extends AppCompatActivity implements NumPadFragment.OnNumberClickedListener {

    private String LOG_TAG = MainActivity.class.getSimpleName();
    private PiAdapter mAdapter;
    private ListView mPiListView;
    private ArrayList<String> mUserPiArrayList;
    private ArrayList<String> mPiDigitsArrayList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get pi and store digits in groups of x digits in an array list
        String pi = readPiFromFile(this);
        pi = pi.replace(".", "");
        mPiDigitsArrayList = new ArrayList<String>(Arrays.asList(StringHelper.splitStringEvery(pi, 4)));

        // Initialize the user's pi array list
        mUserPiArrayList = new ArrayList<String>();
        mUserPiArrayList.add("?.? ? ?");

        // Initialize the adapter and listview
        mAdapter = new PiAdapter(this, mUserPiArrayList);
        mPiListView = (ListView) findViewById(R.id.pi_list_view);
        mPiListView.setAdapter(mAdapter);

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
    public void onNumberClicked(String rowString) {

        // Reset flag for error row to declare no error
        // Removes error display on row after user presses a button
        mAdapter.setInvalidRow(false);

        // Smoothly scroll to bottom of list
        mPiListView.smoothScrollToPosition(mAdapter.getCount());

        // String that is being displayed with ending '?'s
        String displayRowString = rowString;
        while (displayRowString.length() != 4) {
            displayRowString += "?";
        }

        // Add spaces in between characters for the string to be displayed
        displayRowString = StringHelper.addSpacesInBetweenCharacters(displayRowString);

        // Add the decimal point for the first line '3.141'
        if (mAdapter.getCount() == 1) {
            displayRowString = displayRowString.replaceFirst(" ", ".");
        }

        // Update list item visually after user's input
        updateListItem(mAdapter.getCount() - 1, displayRowString);

        // Validate user's input when current row is filled
        if (rowString.length() == 4) {
            // Compare user's input with correct digits of pi
            if (rowString.equals(mPiDigitsArrayList.get(mAdapter.getCount() - 1))) {
                // Add a new placeholder row and smoothly scroll to last list item
                mAdapter.add("? ? ? ?");
                mPiListView.smoothScrollToPosition(mAdapter.getCount());
            }
            else {
                // Show error in row
                mAdapter.setInvalidRow(true);
                updateListItem(mAdapter.getCount() - 1, displayRowString);
            }
        }
    }

}

package com.example.android.pimemorize;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private String LOG_TAG = MainActivity.class.getSimpleName();
    private PiAdapter mAdapter;
    private ListView mPiListView;
    private ArrayList<String> mPiArrayList;
    private Button btnOne;
    private Button btnTwo;
    private Button btnThree;
    private Button btnFour;
    private Button btnFive;
    private Button btnSix;
    private Button btnSeven;
    private Button btnEight;
    private Button btnNine;
    private Button btnZero;
    private Button btnDelete;
    private String mRowString = "";
    private ArrayList<String> mPiDigitsArrayList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOne = (Button) findViewById(R.id.button_one);
        btnTwo = (Button) findViewById(R.id.button_two);
        btnThree = (Button) findViewById(R.id.button_three);
        btnFour = (Button) findViewById(R.id.button_four);
        btnFive = (Button) findViewById(R.id.button_five);
        btnSix = (Button) findViewById(R.id.button_six);
        btnSeven = (Button) findViewById(R.id.button_seven);
        btnEight = (Button) findViewById(R.id.button_eight);
        btnNine = (Button) findViewById(R.id.button_nine);
        btnZero = (Button) findViewById(R.id.button_zero);
        btnDelete = (Button) findViewById(R.id.button_delete);

        btnOne.setOnClickListener((View.OnClickListener) this);
        btnTwo.setOnClickListener((View.OnClickListener) this);
        btnThree.setOnClickListener((View.OnClickListener) this);
        btnFour.setOnClickListener((View.OnClickListener) this);
        btnFive.setOnClickListener((View.OnClickListener) this);
        btnSix.setOnClickListener((View.OnClickListener) this);
        btnSeven.setOnClickListener((View.OnClickListener) this);
        btnEight.setOnClickListener((View.OnClickListener) this);
        btnNine.setOnClickListener((View.OnClickListener) this);
        btnZero.setOnClickListener((View.OnClickListener) this);
        btnDelete.setOnClickListener((View.OnClickListener) this);


        mPiArrayList = new ArrayList<String>();

        String pi = readFromFile(this);
        pi = pi.replace(".", "");
        mPiDigitsArrayList = new ArrayList<String>(Arrays.asList(splitStringEvery(pi, 4)));

        mPiArrayList.add("????");


        mAdapter = new PiAdapter(this, mPiArrayList);
        mPiListView = (ListView) findViewById(R.id.pi_list_view);
        mPiListView.setAdapter(mAdapter);

//        Log.v(LOG_TAG, readFromFile(this));
    }

    private String readFromFile(Context context) {

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

    private String[] splitStringEvery(String str, int interval) {
        int arrayLength = (int) Math.ceil(((str.length() / (double)interval)));
        String[] result = new String[arrayLength];

        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = str.substring(j, j + interval);
            j += interval;
        } //Add the last bit
        result[lastIndex] = str.substring(j);

        return result;
    }

    private String removeLastCharacter(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }

    private void updateListItem(int index, String str) {
        mPiArrayList.set(index, str);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        mPiListView.smoothScrollToPosition(mPiArrayList.size());
        switch (view.getId()) {
            case R.id.button_one: {
                mRowString += "1";
                break;
            }
            case R.id.button_two: {
                mRowString += "2";
                break;
            }
            case R.id.button_three: {
                mRowString += "3";
                break;
            }
            case R.id.button_four: {
                mRowString += "4";
                break;
            }
            case R.id.button_five: {
                mRowString += "5";
                break;
            }
            case R.id.button_six: {
                mRowString += "6";
                break;
            }
            case R.id.button_seven: {
                mRowString += "7";
                break;
            }
            case R.id.button_eight: {
                mRowString += "8";
                break;
            }
            case R.id.button_nine: {
                mRowString += "9";
                break;
            }
            case R.id.button_zero: {
                mRowString += "0";
                break;
            }
            case R.id.button_delete: {
                mRowString = removeLastCharacter(mRowString);
                break;
            }
            default: {
                break;
            }
        }

        String displayRowString = mRowString;
        while (displayRowString.length() != 4) {
            displayRowString += "?";
        }

        updateListItem(mPiArrayList.size() - 1, displayRowString);

        if (mRowString.length() == 4) {
            if (mRowString.equals(mPiDigitsArrayList.get(mPiArrayList.size() - 1))) {
                mAdapter.add("????");
                mPiListView.smoothScrollToPosition(mPiArrayList.size());
            }
            else {
                updateListItem(mPiArrayList.size() - 1, "XXXX");
            }
            mRowString = "";
        }
    }
}

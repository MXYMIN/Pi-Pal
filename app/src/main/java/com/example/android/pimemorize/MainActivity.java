package com.example.android.pimemorize;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private String LOG_TAG = MainActivity.class.getSimpleName();
    private PiAdapter mAdapter;
    private ListView mPiListView;
    private ArrayList<String> mPiArrayList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPiArrayList = new ArrayList<String>();
//        mPiArrayList.add("3141");
//        mPiArrayList.add("5926");
//        mPiArrayList.add("5358");
//        mPiArrayList.add("9793");
//        mPiArrayList.add("2384");
//        mPiArrayList.add("6264");
//        mPiArrayList.add("3383");

        String pi = readFromFile(this);
        pi = pi.replace(".", "");
        mPiArrayList.addAll(Arrays.asList(splitStringEvery(pi, 4)));
        mPiArrayList.remove(1);

        mAdapter = new PiAdapter(this, mPiArrayList);
        mPiListView = (ListView) findViewById(R.id.pi_list_view);
        mPiListView.setAdapter(mAdapter);

        Log.v(LOG_TAG, readFromFile(this));
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

    private String[] splitStringEvery(String s, int interval) {
        int arrayLength = (int) Math.ceil(((s.length() / (double)interval)));
        String[] result = new String[arrayLength];

        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = s.substring(j, j + interval);
            j += interval;
        } //Add the last bit
        result[lastIndex] = s.substring(j);

        return result;
    }
}

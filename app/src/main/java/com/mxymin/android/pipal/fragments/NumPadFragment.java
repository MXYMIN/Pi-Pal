package com.mxymin.android.pipal.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mxymin.android.pipal.Constants;
import com.mxymin.android.pipal.R;
import com.mxymin.android.pipal.helpers.StringHelper;

public class NumPadFragment extends Fragment implements View.OnClickListener{

    private String mRowString = "";
    private int mDigitsPerRow;
    OnNumberClickedListener mCallback;
    private SharedPreferences mSharedPrefs;

    private Button mBtnOne;
    private Button mBtnTwo;
    private Button mBtnThree;
    private Button mBtnFour;
    private Button mBtnFive;
    private Button mBtnSix;
    private Button mBtnSeven;
    private Button mBtnEight;
    private Button mBtnNine;
    private Button mBtnZero;
    private Button mBtnDelete;

    public interface OnNumberClickedListener {
        void onNumberClicked(String rowString);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_num_pad, container, false);

        mBtnOne = (Button) view.findViewById(R.id.button_one);
        mBtnTwo = (Button) view.findViewById(R.id.button_two);
        mBtnThree = (Button) view.findViewById(R.id.button_three);
        mBtnFour = (Button) view.findViewById(R.id.button_four);
        mBtnFive = (Button) view.findViewById(R.id.button_five);
        mBtnSix = (Button) view.findViewById(R.id.button_six);
        mBtnSeven = (Button) view.findViewById(R.id.button_seven);
        mBtnEight = (Button) view.findViewById(R.id.button_eight);
        mBtnNine = (Button) view.findViewById(R.id.button_nine);
        mBtnZero = (Button) view.findViewById(R.id.button_zero);
        mBtnDelete = (Button) view.findViewById(R.id.button_delete);

        mBtnOne.setOnClickListener(this);
        mBtnTwo.setOnClickListener(this);
        mBtnThree.setOnClickListener(this);
        mBtnFour.setOnClickListener(this);
        mBtnFive.setOnClickListener(this);
        mBtnSix.setOnClickListener(this);
        mBtnSeven.setOnClickListener(this);
        mBtnEight.setOnClickListener(this);
        mBtnNine.setOnClickListener(this);
        mBtnZero.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mDigitsPerRow = Integer.parseInt(mSharedPrefs.getString(getResources().getString(R.string.pref_key_digits_per_row), Constants.DEFAULT_DIGITS_PER_ROW));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Update number of digits per row if it was changed in settings and clear current row string
        if (Integer.parseInt(mSharedPrefs.getString(getResources().getString(R.string.pref_key_digits_per_row), Constants.DEFAULT_DIGITS_PER_ROW)) != mDigitsPerRow) {
            mDigitsPerRow = Integer.parseInt(mSharedPrefs.getString(getResources().getString(R.string.pref_key_digits_per_row), Constants.DEFAULT_DIGITS_PER_ROW));
            clearRowString();
            // Re-enable buttons if they were previously disabled in review mode
            if (!mBtnOne.isEnabled()) {
                enableNumPad();
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnNumberClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNumberClickedListener");
        }
    }

    @Override
    public void onClick(View view) {

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
                mRowString = StringHelper.removeLastCharacter(mRowString);
                break;
            }
            default: {
                break;
            }
        }
        mCallback.onNumberClicked(mRowString);
        if (mRowString.length() == mDigitsPerRow) {
            mRowString = "";
        }
    }

    public void clearRowString() {
        mRowString = "";
    }

    public void disableNumPad() {
        mBtnOne.setEnabled(false);
        mBtnTwo.setEnabled(false);
        mBtnThree.setEnabled(false);
        mBtnFour.setEnabled(false);
        mBtnFive.setEnabled(false);
        mBtnSix.setEnabled(false);
        mBtnSeven.setEnabled(false);
        mBtnEight.setEnabled(false);
        mBtnNine.setEnabled(false);
        mBtnZero.setEnabled(false);
        mBtnDelete.setEnabled(false);
    }

    public void enableNumPad() {
        mBtnOne.setEnabled(true);
        mBtnTwo.setEnabled(true);
        mBtnThree.setEnabled(true);
        mBtnFour.setEnabled(true);
        mBtnFive.setEnabled(true);
        mBtnSix.setEnabled(true);
        mBtnSeven.setEnabled(true);
        mBtnEight.setEnabled(true);
        mBtnNine.setEnabled(true);
        mBtnZero.setEnabled(true);
        mBtnDelete.setEnabled(true);
    }
}

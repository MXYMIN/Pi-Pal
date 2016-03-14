package com.example.android.pimemorize;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NumPadFragment extends Fragment implements View.OnClickListener{

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

    // Keep track of digits in current row of pi
    private String mRowString = "";

    OnNumberClickedListener mCallback;

    public interface OnNumberClickedListener {
        public void onNumberClicked(String rowString);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_num_pad, container, false);

        btnOne = (Button) view.findViewById(R.id.button_one);
        btnTwo = (Button) view.findViewById(R.id.button_two);
        btnThree = (Button) view.findViewById(R.id.button_three);
        btnFour = (Button) view.findViewById(R.id.button_four);
        btnFive = (Button) view.findViewById(R.id.button_five);
        btnSix = (Button) view.findViewById(R.id.button_six);
        btnSeven = (Button) view.findViewById(R.id.button_seven);
        btnEight = (Button) view.findViewById(R.id.button_eight);
        btnNine = (Button) view.findViewById(R.id.button_nine);
        btnZero = (Button) view.findViewById(R.id.button_zero);
        btnDelete = (Button) view.findViewById(R.id.button_delete);

        // Set same on click listener for all buttons
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);
        btnFive.setOnClickListener(this);
        btnSix.setOnClickListener(this);
        btnSeven.setOnClickListener(this);
        btnEight.setOnClickListener(this);
        btnNine.setOnClickListener(this);
        btnZero.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        return view;
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
        if (mRowString.length() == 4) {
            // Reset string for current row
            mRowString = "";
        }
    }

}

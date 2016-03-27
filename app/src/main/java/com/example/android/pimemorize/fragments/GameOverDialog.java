package com.example.android.pimemorize.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.pimemorize.R;
import com.example.android.pimemorize.activities.MainMenuActivity;

public class GameOverDialog extends DialogFragment {

    private static final String KEY_FINAL_DIGITS = "KEY_FINAL_DIGITS";
    private static final String KEY_FINAL_ROW = "KEY_FINAL_ROW";

    public interface GameOverDialogListener {
        void onRetryClick();
    }

    GameOverDialogListener mListener;

    public static GameOverDialog newInstance(int finalDigits, int finalRow) {
        GameOverDialog gameOverDialog = new GameOverDialog();

        Bundle args = new Bundle();
        args.putInt(KEY_FINAL_DIGITS, finalDigits);
        args.putInt(KEY_FINAL_ROW, finalRow);
        gameOverDialog.setArguments(args);

        return gameOverDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_game_over, null);
        TextView finalDigitsTextView = (TextView) view.findViewById(R.id.digits_dialog_text_view);
        TextView finalRowTextView = (TextView) view.findViewById(R.id.row_dialog_text_view);
        Button retryButton = (Button) view.findViewById(R.id.retry_button);
        Button menuButton = (Button) view.findViewById(R.id.menu_button);
        Button reviewButton = (Button) view.findViewById(R.id.review_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        finalDigitsTextView.setText(Integer.toString(getArguments().getInt(KEY_FINAL_DIGITS)));
        finalRowTextView.setText(Integer.toString(getArguments().getInt(KEY_FINAL_ROW)));

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRetryClick();
                dialog.dismiss();
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainMenuActivity.class);
                startActivity(intent);
            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    // Override the Fragment.onAttach() method to instantiate the GameOverDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the GameOverDialogListener so we can send events to the host
            mListener = (GameOverDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement GameOverDialogListener");
        }
    }
}

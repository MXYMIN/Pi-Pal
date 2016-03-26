package com.example.android.pimemorize.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.pimemorize.R;

public class GameOverDialog extends DialogFragment {

    public static GameOverDialog newInstance(int finalDigits, int finalRow) {
        GameOverDialog gameOverDialog = new GameOverDialog();

        Bundle args = new Bundle();
        args.putInt("FINAL_DIGITS", finalDigits);
        args.putInt("FINAL_ROW", finalRow);
        gameOverDialog.setArguments(args);

        return gameOverDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_game_over, null);
        TextView finalDigitsTextView = (TextView) view.findViewById(R.id.digits_dialog_text_view);
        TextView finalRowTextView = (TextView) view.findViewById(R.id.row_dialog_text_view);

        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        finalDigitsTextView.setText(Integer.toString(getArguments().getInt("FINAL_DIGITS")));
        finalRowTextView.setText(Integer.toString(getArguments().getInt("FINAL_ROW")));

        Button reviewButton = (Button) view.findViewById(R.id.review_button);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }
}

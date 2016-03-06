package com.example.android.pimemorize;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by michael on 2016-03-06.
 */
public class PiAdapter extends ArrayAdapter<String> {

    public PiAdapter(Context context, ArrayList<String> piRows) {
        super(context, 0, piRows);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String row = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pi_list_item, parent, false);
        }

        // Lookup view for data population
        TextView piDigits = (TextView) convertView.findViewById(R.id.pi_row_digits_text_view);
        // Populate the data into the template view using the data object

        piDigits.setText(row);

        // Return the completed view to render on screen
        return convertView;
    }
}

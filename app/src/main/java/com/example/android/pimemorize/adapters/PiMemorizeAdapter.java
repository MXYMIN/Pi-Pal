package com.example.android.pimemorize.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.pimemorize.R;
import com.example.android.pimemorize.helpers.StringHelper;

import java.util.ArrayList;

/**
 * Created by michael on 2016-03-27.
 */
public class PiMemorizeAdapter extends ArrayAdapter<String> {

    private Context mContext;

    public PiMemorizeAdapter(Context context, ArrayList<String> piRows) {
        super(context, 0, piRows);

        mContext = context;
    }

    // getView is called whenever a new item is displayed on screen including old items
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        String row = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pi_list_item, parent, false);
        }

        // Set list item height programmatically to only show 6 items on screen at a time
        int listItemHeight = parent.getHeight() / 6;
        ViewGroup.LayoutParams params = convertView.getLayoutParams();
        params.height = listItemHeight;
        convertView.requestLayout();

        // Lookup view for data population
        TextView piDigits = (TextView) convertView.findViewById(R.id.pi_row_digits_text_view);

        // Add spaces to pi string
        row = StringHelper.addSpacesInBetweenCharacters(row);
        // Re-add the decimal if first row of pi
        if (position == 0) {
            row = row.replaceFirst(" ", ".");
        }

        piDigits.setText(row);
        piDigits.setTextColor(mContext.getResources().getColor(R.color.colorOldRow));

        // Return the completed view to render on screen
        return convertView;
    }

}

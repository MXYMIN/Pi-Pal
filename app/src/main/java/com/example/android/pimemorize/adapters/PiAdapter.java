package com.example.android.pimemorize.adapters;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.pimemorize.Constants;
import com.example.android.pimemorize.R;
import com.example.android.pimemorize.helpers.StringHelper;

import java.util.ArrayList;

/**
 * Created by michael on 2016-03-06.
 */
public class PiAdapter extends ArrayAdapter<String> {

    private int lastAdded;
    private Context mContext;
    public PiAdapter(Context context, ArrayList<String> piRows) {
        super(context, 0, piRows);
        lastAdded = piRows.size() - 1;
        mContext = context;

    }

    // Update lastAdded whenever add is called
    @Override
    public void add(String str) {
        lastAdded = getCount();
        super.add(str);
    };

    // getView is called whenever a new item is displayed on screen including old items
    // To prevent messed up text colours, programatically change the colours whenever a "new" view appears
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        String row = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pi_list_item, parent, false);
        }

        // Set list item height programmatically to only show 3 items on screen at a time
        int listItemHeight = parent.getHeight() / 3;
        ViewGroup.LayoutParams params = convertView.getLayoutParams();
        params.height = listItemHeight;
        convertView.requestLayout();

        // Lookup view for data population
        TextView piDigits = (TextView) convertView.findViewById(R.id.pi_row_digits_text_view);

        // Populate the data into the template view using the data object
        // Using Spanned object to accept html attributes
        Spanned spannedRow = Html.fromHtml(StringHelper.boldFirstOccurenceOfSubstring(row, "?"));
        piDigits.setText(spannedRow);

        // Set text colour
        if (row.equals(Constants.ERROR_ROW)) {
            piDigits.setTextColor(mContext.getResources().getColor(R.color.colorError));
        }
        else {
            if (position == lastAdded) {
                piDigits.setTextColor(mContext.getResources().getColor(R.color.colorCurrentRow));
            }
            else {
                piDigits.setTextColor(mContext.getResources().getColor(R.color.colorOldRow));
            }
        }

        // Return the completed view to render on screen
        return convertView;
    }

    // Update lastAdded whenever insert is called
    @Override
    public void insert(String str, int index) {
        lastAdded = index;
        super.insert(str, index);
    };
}

package com.six.the.from.izzo.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.Exercise;


public class ExerciseArrayAdapter extends ArrayAdapter<Exercise> {

    public ExerciseArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Get the data item for this position
        Exercise exercise = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_exercise, parent, false);
        }
        // Lookup view for data population
        TextView txtView_Name = (TextView) view.findViewById(R.id.name);
        TextView txtView_Type = (TextView) view.findViewById(R.id.type);

        // Populate the data into the template view using the data object
        txtView_Name.setText(exercise.getName());
        txtView_Type.setText(exercise.getType());

        // Return the completed view to render on screen
        return view;
    }
}

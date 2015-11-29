package com.six.the.from.izzo.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.Exercise;


public class WeightTrainingExerciseArrayAdapter extends ArrayAdapter<Exercise> {

    public WeightTrainingExerciseArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Get the data item for this position
        Exercise exercise = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_weighttraining_exercise, parent, false);
        }
        // Lookup view for data population
        TextView txtView_Name = (TextView) view.findViewById(R.id.name);
        TextView txtView_Sets = (TextView) view.findViewById(R.id.num_sets);
        TextView txtView_Reps = (TextView) view.findViewById(R.id.num_reps);
        TextView txtView_Weight = (TextView) view.findViewById(R.id.weight);

        // Populate the data into the template view using the data object
        txtView_Name.setText(exercise.getName());
        txtView_Sets.setText(String.valueOf(exercise.getNumSets()));
        txtView_Reps.setText(String.valueOf(exercise.getNumReps()[0]));
        txtView_Weight.setText(String.valueOf(exercise.getWeight()[0]) + " ");

        // Return the completed view to render on screen
        return view;
    }
}

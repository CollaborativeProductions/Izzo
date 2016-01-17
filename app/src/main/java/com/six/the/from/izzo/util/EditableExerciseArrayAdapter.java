package com.six.the.from.izzo.util;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.Exercise;


public class EditableExerciseArrayAdapter extends ArrayAdapter<Exercise> {
    protected final Context applicationContext;

    public EditableExerciseArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.applicationContext = context;
    }

    public EditableExerciseArrayAdapter(Context context, int dummyLayoutResource, Exercise[] exercises) {
        super(context, dummyLayoutResource, exercises);
        this.applicationContext = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Get the data item for this position
        Exercise exercise = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (exercise.getType().equals("Cardio")) {
                view = inflater.inflate(R.layout.list_item_editable_cardio_exercise, parent, false);
                view.setTag("Cardio Exercise");

                TextView txtView_Name = (TextView) view.findViewById(R.id.name);
                final EditText et_Distance = (EditText) view.findViewById(R.id.distance);
                setOnEditorActionListener(et_Distance);
                final EditText et_Duration = (EditText) view.findViewById(R.id.duration);
                setOnEditorActionListener(et_Duration);

                txtView_Name.setText(exercise.getName());
                et_Distance.setText(String.valueOf(exercise.getDistance()));
                et_Duration.setText(String.valueOf(exercise.getDuration()));
            } else if (exercise.getType().equals("Weight")) {
                view = inflater.inflate(R.layout.list_item_editable_weight_training_exercise, parent, false);
                view.setTag("Weight Training Exercise");

                TextView txtView_Name = (TextView) view.findViewById(R.id.name);
                final EditText et_Sets = (EditText) view.findViewById(R.id.num_sets);
                setOnEditorActionListener(et_Sets);
                final EditText et_Reps = (EditText) view.findViewById(R.id.num_reps);
                setOnEditorActionListener(et_Reps);
                final EditText et_Weight = (EditText) view.findViewById(R.id.weight);
                setOnEditorActionListener(et_Weight);

                txtView_Name.setText(exercise.getName());
                et_Sets.setText(String.valueOf(exercise.getNumSets()));
                et_Reps.setText(String.valueOf(exercise.getNumReps()[0]));
                et_Weight.setText(String.valueOf(exercise.getWeight()[0]));
            }
        }

        return view;
    }

    private void setOnEditorActionListener(final EditText editText) {
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) applicationContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    editText.clearFocus();
                }
                return false;
            }
        });
    }
}

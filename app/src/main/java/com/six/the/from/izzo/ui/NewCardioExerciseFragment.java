package com.six.the.from.izzo.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.Exercise;
import com.six.the.from.izzo.spinnerwheel.AbstractWheel;
import com.six.the.from.izzo.spinnerwheel.adapters.ArrayWheelAdapter;
import com.six.the.from.izzo.spinnerwheel.adapters.NumericWheelAdapter;


public class NewCardioExerciseFragment extends DialogFragment {
    AbstractWheel exerciseSelectorWheel;
    ArrayWheelAdapter<String> exercisesAdapter;
    AbstractWheel durationSelectorWheel;
    NumericWheelAdapter durationAdapter;
    AbstractWheel distanceSelectorWheel;
    NumericWheelAdapter distanceAdapter;

    public interface NewCardioExerciseDialogListener {
        void onFinishNewCardioExerciseDialog(Exercise exercise);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_new_cardio_exercise, null);

        Button btnClose = (Button) view.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

        Button addExercise = (Button) view.findViewById(R.id.add_exercise);
        addExercise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addExercise();
            }
        });

        exerciseSelectorWheel = (AbstractWheel) view.findViewById(R.id.select_exercise);
        exercisesAdapter = new ArrayWheelAdapter<>(this.getContext(), new String[] {"Run", "Swim", "Bike", "Walk", "Stairs"});
        exercisesAdapter.setItemResource(R.layout.wheel_text_centered_dark_back_exercise);
        exercisesAdapter.setItemTextResource(R.id.text);
        exerciseSelectorWheel.setViewAdapter(exercisesAdapter);

        distanceSelectorWheel = (AbstractWheel) view.findViewById(R.id.select_distance);
        distanceAdapter = new NumericWheelAdapter(this.getContext(), 0, 999, "%d");
        distanceAdapter.setItemResource(R.layout.wheel_text_centered_dark_back);
        distanceAdapter.setItemTextResource(R.id.text);
        distanceSelectorWheel.setViewAdapter(distanceAdapter);

        durationSelectorWheel = (AbstractWheel) view.findViewById(R.id.select_duration);
        durationAdapter = new NumericWheelAdapter(this.getContext(), 0, 999, "%d");
        durationAdapter.setItemResource(R.layout.wheel_text_centered_dark_back);
        durationAdapter.setItemTextResource(R.id.text);
        durationSelectorWheel.setViewAdapter(durationAdapter);

        builder.setView(view);

        return builder.create();
    }

    public void addExercise() {
        NewCardioExerciseDialogListener listener = (NewCardioExerciseDialogListener) getActivity();
        listener.onFinishNewCardioExerciseDialog(
                new Exercise(
                        String.valueOf(exercisesAdapter.getItemText(exerciseSelectorWheel.getCurrentItem())),
                        Integer.parseInt(String.valueOf(distanceAdapter.getItemText(distanceSelectorWheel.getCurrentItem()))),
                        Integer.parseInt(String.valueOf(durationAdapter.getItemText(durationSelectorWheel.getCurrentItem())))
                )
        );
        dismiss();
    }
}

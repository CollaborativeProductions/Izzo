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


public class NewWeightTrainingExerciseFragment extends DialogFragment {
    AbstractWheel exerciseSelectorWheel;
    ArrayWheelAdapter<String> exercisesAdapter;
    AbstractWheel numRepsSelectorWheel;
    NumericWheelAdapter numRepsAdapter;
    AbstractWheel numSetsSelectorWheel;
    NumericWheelAdapter numSetsAdapter;
    AbstractWheel weightSelectorWheel;
    NumericWheelAdapter weightAdapter;

    public interface NewWeightTrainingExerciseDialogListener {
        void onFinishNewWeightTrainingExerciseDialog(Exercise exercise);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.popup_new_weight_training_exercise, null);

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

        numSetsSelectorWheel = (AbstractWheel) view.findViewById(R.id.select_num_sets);
        numSetsAdapter = new NumericWheelAdapter(this.getContext(), 0, 99, "%d");
        numSetsAdapter.setItemResource(R.layout.wheel_text_centered_dark_back);
        numSetsAdapter.setItemTextResource(R.id.text);
        numSetsSelectorWheel.setViewAdapter(numSetsAdapter);

        numRepsSelectorWheel = (AbstractWheel) view.findViewById(R.id.select_num_reps);
        numRepsAdapter = new NumericWheelAdapter(this.getContext(), 0, 99, "%d");
        numRepsAdapter.setItemResource(R.layout.wheel_text_centered_dark_back);
        numRepsAdapter.setItemTextResource(R.id.text);
        numRepsSelectorWheel.setViewAdapter(numRepsAdapter);

        weightSelectorWheel = (AbstractWheel) view.findViewById(R.id.select_weight);
        weightAdapter = new NumericWheelAdapter(this.getContext(), 0, 99, "%d");
        weightAdapter.setItemResource(R.layout.wheel_text_centered_dark_back);
        weightAdapter.setItemTextResource(R.id.text);
        weightSelectorWheel.setViewAdapter(weightAdapter);

        builder.setView(view);

        return builder.create();
    }

    public void addExercise() {
        NewWeightTrainingExerciseDialogListener listener = (NewWeightTrainingExerciseDialogListener) getActivity();
        listener.onFinishNewWeightTrainingExerciseDialog(
                new Exercise(
                        String.valueOf(exercisesAdapter.getItemText(exerciseSelectorWheel.getCurrentItem())),
                        Integer.parseInt(String.valueOf(numSetsAdapter.getItemText(numSetsSelectorWheel.getCurrentItem()))),
                        Integer.parseInt(String.valueOf(numRepsAdapter.getItemText(numRepsSelectorWheel.getCurrentItem()))),
                        Integer.parseInt(String.valueOf(weightAdapter.getItemText(weightSelectorWheel.getCurrentItem())))
                )
        );
        dismiss();
    }
}

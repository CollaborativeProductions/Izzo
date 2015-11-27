package com.six.the.from.izzo.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.six.the.from.izzo.R;

import java.util.ArrayList;


public class SelectProgramTeamFragment extends DialogFragment {
    ArrayAdapter<String> teamsSpinnerArrayAdapter;
    int spinnerItemSelectedPos;

    public interface SaveProgramToTeamDialogListener {
        void onFinishSelectProgramTeamDialog(int spinnerItemPosition);

        void onFinishSelectProgramTeamDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_select_program_team, null);

        Bundle bundle = this.getArguments();
        ArrayList<String> teamsArray = bundle.getStringArrayList("allTeamsArrayList");

        teamsSpinnerArrayAdapter = new ArrayAdapter<>(
                this.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                teamsArray
        );
        teamsSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner teamsSpinner = (Spinner) view.findViewById(R.id.teams_spinner);
        teamsSpinner.setAdapter(teamsSpinnerArrayAdapter);
        teamsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerItemSelectedPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        final CheckBox cbHasTeam = (CheckBox) view.findViewById(R.id.checkbox_has_team);
        cbHasTeam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                     teamsSpinner.setEnabled(!isChecked);
                     teamsSpinner.setClickable(!isChecked);
                 }
             }
        );

        Button btnSaveProgram = (Button) view.findViewById(R.id.save_program);
        btnSaveProgram.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addTeam(cbHasTeam.isAttachedToWindow());
            }
        });

        builder.setView(view);

        return builder.create();
    }

    public void addTeam(Boolean hasTeam) {
        SaveProgramToTeamDialogListener listener = (SaveProgramToTeamDialogListener) getActivity();
        if (hasTeam) {
            listener.onFinishSelectProgramTeamDialog(spinnerItemSelectedPos);
        } else {
            listener.onFinishSelectProgramTeamDialog();
        }
        dismiss();
    }
}

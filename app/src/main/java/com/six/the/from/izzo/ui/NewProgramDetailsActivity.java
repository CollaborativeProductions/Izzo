package com.six.the.from.izzo.ui;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.six.the.from.izzo.util.CardioExerciseArrayAdapter;

import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.Exercise;
import com.six.the.from.izzo.util.WeightTrainingExerciseArrayAdapter;
import com.six.the.from.izzo.ui.NewCardioExerciseFragment.NewCardioExerciseDialogListener;

public class NewProgramDetailsActivity extends ActionBarActivity implements NewCardioExerciseDialogListener {
    CardioExerciseArrayAdapter cardioExerciseArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_program_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("programName"));
        initViews();
    }

    private void initViews() {
        initCardioListView();
        initWeightTrainingListView();
    }

    private void initCardioListView() {
        cardioExerciseArrayAdapter = new CardioExerciseArrayAdapter(
                this,
                R.layout.cardio_exercise_list_item);
        ListView listView = (ListView) findViewById(R.id.lv_cardio_exercises);
        listView.setAdapter(cardioExerciseArrayAdapter);
    }

    private void initWeightTrainingListView() {
        ListView listView = (ListView) findViewById(R.id.lv_weighttraining_exercises);
        final WeightTrainingExerciseArrayAdapter weightTrainingExerciseArrayAdapter = new WeightTrainingExerciseArrayAdapter(
            this,
            R.layout.weighttraining_exercise_list_item);
        Exercise exercise = new Exercise("Bench Press", new int[]{20, 40});
        weightTrainingExerciseArrayAdapter.add(exercise);
        exercise = new Exercise("Skull Crusher", new int[]{60, 80, 100});
        weightTrainingExerciseArrayAdapter.add(exercise);
        exercise = new Exercise("1-Arm Tricep Curl", new int[]{60, 80, 100});
        weightTrainingExerciseArrayAdapter.add(exercise);
        exercise = new Exercise("Tricep Pull Down", new int[]{60, 80, 100, 120});
        weightTrainingExerciseArrayAdapter.add(exercise);
        exercise = new Exercise("Dumbbell Lunges", new int[]{60, 80, 100});
        weightTrainingExerciseArrayAdapter.add(exercise);
        listView.setAdapter(weightTrainingExerciseArrayAdapter);
    }

    public void addNewWeightTrainingExercise(View v) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        NewCardioExerciseFragment newCardioExerciseFragment = new NewCardioExerciseFragment();
        newCardioExerciseFragment.show(ft, "dialog");
    }

    public void onFinishNewCardioExerciseDialog(Exercise exercise) {
        cardioExerciseArrayAdapter.add(exercise);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_program_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
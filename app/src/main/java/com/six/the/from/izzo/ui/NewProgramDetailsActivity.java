package com.six.the.from.izzo.ui;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.six.the.from.izzo.util.CardioExerciseArrayAdapter;

import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.Exercise;
import com.six.the.from.izzo.util.NewWeightTrainingExercisePopupWindow;
import com.six.the.from.izzo.util.WeightTrainingExerciseArrayAdapter;

public class NewProgramDetailsActivity extends ActionBarActivity {
    View backDimLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_program_details);
        initViews();
    }

    private void initViews() {
        backDimLayout = (RelativeLayout) findViewById(R.id.back_dim_layout);
        initCardioListView();
        initWeightTrainingListView();
    }

    private void initCardioListView() {
        ListView listView = (ListView) findViewById(R.id.lv_cardio_exercises);
        final CardioExerciseArrayAdapter cardioExerciseArrayAdapter = new CardioExerciseArrayAdapter(
            this,
            R.layout.cardio_exercise_list_item);
        Exercise exercise = new Exercise("Running", 20, 40);
        cardioExerciseArrayAdapter.add(exercise);
        Exercise exercise2 = new Exercise("Swimming", 60, 100);
        cardioExerciseArrayAdapter.add(exercise2);
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
        WindowManager wm = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels - 60;
        int height = metrics.heightPixels - 500;

        NewWeightTrainingExercisePopupWindow newWeightTrainingExercisePopupWindow = new NewWeightTrainingExercisePopupWindow(
                this.getApplicationContext(),
                backDimLayout,
                width,
                height);
        newWeightTrainingExercisePopupWindow.show(this.findViewById(R.id.activity_new_program_details), 0, 0);
        backDimLayout.setVisibility(View.VISIBLE);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

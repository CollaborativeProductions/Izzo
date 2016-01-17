package com.six.the.from.izzo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.parse.ParseObject;
import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.CurrentAthlete;
import com.six.the.from.izzo.models.Exercise;
import com.six.the.from.izzo.util.EditableExerciseArrayAdapter;
import com.six.the.from.izzo.util.ParseUtils;
import com.six.the.from.izzo.util.FetchProgramExercisesStatusFetcher;

import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

import roboguice.activity.RoboActionBarActivity;

public class WorkoutActivity extends RoboActionBarActivity {
    @Inject
    CurrentAthlete currentAthlete;

    EditableExerciseArrayAdapter exerciseArrayAdapter;
    ListView listView;
    ParseObject programParseObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        initViews();
        getSupportActionBar().setTitle(getIntent().getStringExtra("programName"));
        new FetchProgramExercisesThread().start();
    }

    private void initViews() {
        exerciseArrayAdapter = new EditableExerciseArrayAdapter(this, R.layout.list_item_editable_cardio_exercise);
        listView = (ListView) findViewById(R.id.lv_exercises);
        listView.setAdapter(exerciseArrayAdapter);

        Button btnCompleteWorkout = (Button) findViewById(R.id.btn_complete_workout);
        btnCompleteWorkout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                saveWorkout();
            }
        });
    }

    private void saveWorkout() {
        exerciseArrayAdapter = new EditableExerciseArrayAdapter(this, R.layout.list_item_editable_cardio_exercise, getExercisesToSave());
        new SaveWorkout().start();
    }

    private Exercise[] getExercisesToSave() {
        View listItemView;
        Exercise exercise;
        Exercise[] exercisesToSave = new Exercise[listView.getChildCount()];
        for (int i = 0; i < listView.getChildCount(); i++) {
            listItemView = listView.getChildAt(i);
            exercise = exerciseArrayAdapter.getItem(i);
            if (listItemView.getTag().equals("Cardio Exercise")) {
                EditText et_Distance = (EditText) listItemView.findViewById(R.id.distance);
                EditText et_Duration = (EditText) listItemView.findViewById(R.id.duration);

                int distance = Integer.parseInt(et_Distance.getText().toString());
                int duration = Integer.parseInt(et_Duration.getText().toString());

                exercise.setDistance(distance);
                exercise.setDuration(duration);
            } else if (listItemView.getTag().equals("Weight Training Exercise")) {
                EditText et_Sets = (EditText) listItemView.findViewById(R.id.num_sets);
                EditText et_Reps = (EditText) listItemView.findViewById(R.id.num_reps);
                EditText et_Weight = (EditText) listItemView.findViewById(R.id.weight);

                int num_sets = Integer.parseInt(et_Sets.getText().toString());
                int reps = Integer.parseInt(et_Reps.getText().toString());
                int weight = Integer.parseInt(et_Weight.getText().toString());

                int[] repsArr = new int[num_sets];
                for (int j = 0; j < num_sets; j++) {
                    repsArr[j] = reps;
                }

                int[] weightsArr = new int[num_sets];
                for (int j = 0; j < num_sets; j++) {
                    weightsArr[j] = weight;
                }

                exercise.setNumReps(repsArr);
                exercise.setWeight(weightsArr);
            }
            exercisesToSave[i] = exercise;
        }
        return exercisesToSave;
    }

    private void finishActivity() {
        finish();
        Toast.makeText(this.getApplicationContext(), "Congratulations! Beast mode activated!", Toast.LENGTH_LONG);
    }

    private class FetchProgramExercisesThread extends Thread {
        private final FetchProgramExercisesStatusFetcher fetcher = new FetchProgramExercisesStatusFetcher();

        public FetchProgramExercisesThread() { }

        public void run() {
            ParseUtils.fetchProgramExercises(fetcher, getIntent().getStringExtra("programId"));

            while (fetcher.fetching) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    programParseObject = fetcher.programParseObj;
                    if (!fetcher.fetching && fetcher.exerciseParseObjs.size() > 0) {
                        Exercise exercise;
                        for (ParseObject exerciseParseObj : fetcher.exerciseParseObjs) {
                            if (exerciseParseObj.getString("type").equals("Cardio")) {
                                exercise = new Exercise(
                                        exerciseParseObj.getString("name"),
                                        exerciseParseObj.getInt("distance"),
                                        exerciseParseObj.getInt("duration")
                                );
                            } else {
                                List<Object> repsList = exerciseParseObj.getList("reps");
                                List<Object> weightList = exerciseParseObj.getList("weight");
                                int[] repsArray = ArrayUtils.toPrimitive(repsList.toArray(new Integer[repsList.size()]));
                                int[] weightArray = ArrayUtils.toPrimitive(weightList.toArray(new Integer[weightList.size()]));
                                exercise = new Exercise(
                                        exerciseParseObj.getString("name"),
                                        repsArray,
                                        weightArray
                                );
                            }
                            exerciseArrayAdapter.add(exercise);
                            exerciseArrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    }

    private class SaveWorkout extends Thread {
        private final SaveWorkoutStatusFetcher fetcher = new SaveWorkoutStatusFetcher();

        public SaveWorkout() { }

        public void run() {
            ParseUtils.saveWorkout(exerciseArrayAdapter, fetcher, currentAthlete.getParseObject(), programParseObject);

            while (fetcher.saving) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!fetcher.saving) {
                        finishActivity();
                    }
                }
            });
        }
    }

    public class SaveWorkoutStatusFetcher {
        public volatile boolean saving;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workout, menu);
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

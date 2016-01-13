package com.six.the.from.izzo.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.ParseObject;
import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.Exercise;
import com.six.the.from.izzo.util.EditableExerciseArrayAdapter;
import com.six.the.from.izzo.util.ParseUtils;
import com.six.the.from.izzo.util.FetchProgramExercisesStatusFetcher;

import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public class WorkoutActivity extends ActionBarActivity {
    EditableExerciseArrayAdapter exerciseArrayAdapter;

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
        ListView listView = (ListView) findViewById(R.id.lv_exercises);
        listView.setAdapter(exerciseArrayAdapter);
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

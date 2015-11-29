package com.six.the.from.izzo.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.ParseObject;
import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.Exercise;
import com.six.the.from.izzo.util.ExerciseArrayAdapter;
import com.six.the.from.izzo.util.ParseUtils;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActionBarActivity;


public class CurrentProgramActivity extends RoboActionBarActivity {
    ImageView bmpImageView;
    ExerciseArrayAdapter exerciseArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_current_program);
        initViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("programName"));
        if (getIntent().hasExtra("iconImageUrl")) {
            Picasso.with(this.getApplicationContext()).load(getIntent().getStringExtra("iconImageUrl")).into(bmpImageView);
        }
        new FetchProgramExercisesThread().start();
    }

    private void initViews() {
        bmpImageView = (ImageView) findViewById(R.id.img_program_logo);
        exerciseArrayAdapter = new ExerciseArrayAdapter(this, R.layout.cardio_exercise_list_item);
        ListView listView = (ListView) findViewById(R.id.lv_exercises);
        listView.setAdapter(exerciseArrayAdapter);

//        Button btnNewWorkout = (Button) findViewById(R.id.btn_start_workout);
//        btnAddNewMembers.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                launchActivity(
//                        ContactsListActivity.class,
//                        teamParseObject.getObjectId(),
//                        teamParseObject.getString("name")
//                );
//            }
//        });
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

    public class FetchProgramExercisesStatusFetcher {
        public volatile boolean fetching;
        public List<ParseObject> exerciseParseObjs = new ArrayList<>();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_current_program, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

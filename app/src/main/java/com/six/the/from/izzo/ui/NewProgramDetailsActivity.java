package com.six.the.from.izzo.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.CurrentAthlete;
import com.six.the.from.izzo.models.Exercise;
import com.six.the.from.izzo.ui.NewCardioExerciseFragment.NewCardioExerciseDialogListener;
import com.six.the.from.izzo.ui.NewWeightTrainingExerciseFragment.NewWeightTrainingExerciseDialogListener;
import com.six.the.from.izzo.util.CardioExerciseArrayAdapter;
import com.six.the.from.izzo.util.ParseUtils;
import com.six.the.from.izzo.util.WeightTrainingExerciseArrayAdapter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import javax.inject.Inject;

import roboguice.activity.RoboActionBarActivity;


public class NewProgramDetailsActivity extends RoboActionBarActivity implements NewCardioExerciseDialogListener, NewWeightTrainingExerciseDialogListener {
    @Inject
    CurrentAthlete currentAthlete;
    CardioExerciseArrayAdapter cardioExerciseArrayAdapter;
    WeightTrainingExerciseArrayAdapter weightTrainingExerciseArrayAdapter;
    Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_program_details);
        applicationContext = this.getApplicationContext();
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
        weightTrainingExerciseArrayAdapter = new WeightTrainingExerciseArrayAdapter(
            this,
            R.layout.weighttraining_exercise_list_item);
        ListView listView = (ListView) findViewById(R.id.lv_weighttraining_exercises);
        listView.setAdapter(weightTrainingExerciseArrayAdapter);
    }

    public void addNewWeightTrainingExercise(View v) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        NewWeightTrainingExerciseFragment newWeightTrainingExerciseFragment = new NewWeightTrainingExerciseFragment();
        newWeightTrainingExerciseFragment.show(ft, "newWeightTrainingExerciseDialog");
    }

    public void addNewCardioExercise(View v) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        NewCardioExerciseFragment newCardioExerciseFragment = new NewCardioExerciseFragment();
        newCardioExerciseFragment.show(ft, "newCardioExerciseDialog");
    }

    public void onFinishNewCardioExerciseDialog(Exercise exercise) {
        cardioExerciseArrayAdapter.add(exercise);
    }

    public void onFinishNewWeightTrainingExerciseDialog(Exercise exercise) {
        weightTrainingExerciseArrayAdapter.add(exercise);
    }

    private void saveProgram() {
        new SaveProgramThread().start();
    }

    private class SaveProgramThread extends Thread {
        private final SaveProgramStatusFetcher fetcher = new SaveProgramStatusFetcher();

        public SaveProgramThread() { }

        public void run() {
            ParseFile parseFile = null;
            if (getIntent().hasExtra("imageFile")) {
                try {
                    Bitmap bmpImage = BitmapFactory.decodeStream(applicationContext.openFileInput("izzoTeamIconImage"));
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    bmpImage.compress(Bitmap.CompressFormat.PNG, 100, bs);
                    parseFile = new ParseFile("imageData.txt", bs.toByteArray());
                    parseFile.saveInBackground();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            ParseUtils.saveProgram(
                    getIntent().getStringExtra("programName"),
                    cardioExerciseArrayAdapter,
                    weightTrainingExerciseArrayAdapter,
                    parseFile,
                    fetcher,
                    currentAthlete.getParseObject()
            );

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
                        Toast.makeText(getApplicationContext(), "Saved.. Check Parse DB", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public class SaveProgramStatusFetcher {
        public volatile boolean saving;
        public volatile ParseObject programParseObj;
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
            case R.id.action_done:
                saveProgram();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
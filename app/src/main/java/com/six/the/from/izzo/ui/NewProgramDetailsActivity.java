package com.six.the.from.izzo.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.six.the.from.izzo.R;

public class NewProgramDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_program_details);
        initViews();
    }

    private void initViews() {
        initCardioListView();
        initWeightTrainingListView();
    }

    private void initCardioListView() {
        ListView listView = (ListView) findViewById(R.id.lv_cardio_exercises);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        arrayAdapter.add("Running");
        arrayAdapter.add("Swimming");
        listView.setAdapter(arrayAdapter);
    }

    private void initWeightTrainingListView() {
        ListView listView = (ListView) findViewById(R.id.lv_weighttraining_exercises);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        arrayAdapter.add("Bench Press");
        arrayAdapter.add("1-Arm Tricep Curl");
        arrayAdapter.add("Leg Extension");
        arrayAdapter.add("Squat (Olympic)");
        arrayAdapter.add("Hammer Curl");
        arrayAdapter.add("Leg Press");
        listView.setAdapter(arrayAdapter);
    }

    public void addNewWeightTrainingExercise(View v) {
        Toast.makeText(getApplicationContext(), "this is my Toast message!!! =)",
                Toast.LENGTH_LONG).show();
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

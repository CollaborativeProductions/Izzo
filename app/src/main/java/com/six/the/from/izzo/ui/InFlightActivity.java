package com.six.the.from.izzo.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.CurrentAthlete;

import javax.inject.Inject;

import roboguice.activity.RoboActionBarActivity;


public class InFlightActivity extends RoboActionBarActivity {
    @Inject
    CurrentAthlete currentAthlete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_flight);

        if (!getIntent().hasExtra("teamName")) return;

        TextView txtTeamName = (TextView) findViewById(R.id.txt_team_name);
        txtTeamName.setText(getIntent().getStringExtra("teamName"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_in_flight, menu);
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

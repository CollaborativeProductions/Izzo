package com.six.the.from.izzo.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.CurrentAthlete;
import com.six.the.from.izzo.models.Team;
import com.six.the.from.izzo.util.ParseUtils;
import com.six.the.from.izzo.util.TeamArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import roboguice.activity.RoboActionBarActivity;


public class ProgramsActivity extends RoboActionBarActivity {
    @Inject
    CurrentAthlete currentAthlete;

    TeamArrayAdapter teamArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);
        ListView lvTeamsList = (ListView) findViewById(R.id.team_list_view);
        teamArrayAdapter = new TeamArrayAdapter(this, R.layout.team_list_item);
        lvTeamsList.setAdapter(teamArrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new FetchTeamsInfoThread().start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_programs, menu);
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

    private class FetchTeamsInfoThread extends Thread {
        private final TeamsInfoFetcher fetcher = new TeamsInfoFetcher();

        public FetchTeamsInfoThread() { }

        public void run() {
            ParseUtils.fetchCurrentAthleteTeams(fetcher, currentAthlete.getParseObject());

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
                    for (Team team : fetcher.teamList) {
                        teamArrayAdapter.add(team);
                    }
                    teamArrayAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public static class TeamsInfoFetcher {
        public boolean fetching;
        public List<Team> teamList = new ArrayList<>();
    }
}

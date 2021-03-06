package com.six.the.from.izzo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseObject;
import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.CurrentAthlete;
import com.six.the.from.izzo.models.Team;
import com.six.the.from.izzo.util.ParseUtils;
import com.six.the.from.izzo.util.TeamArrayAdapter;
import com.six.the.from.izzo.util.TeamsInfoFetcher;

import javax.inject.Inject;

import roboguice.activity.RoboActionBarActivity;


public class AllProgramsActivity extends RoboActionBarActivity {
    @Inject
    CurrentAthlete currentAthlete;
    RelativeLayout relativeLayout;
    Context applicationContext;
    TeamArrayAdapter teamArrayAdapter;
    TextView txtViewNoPrograms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_programs);
        applicationContext = this.getApplicationContext();
        initListView();
        fetchCurrentAthleteTeamInfo();
    }

    private void initListView() {
        ListView lvTeamsList = (ListView) findViewById(R.id.team_list_view);
        teamArrayAdapter = new TeamArrayAdapter(this, R.layout.list_item_team);
        lvTeamsList.setAdapter(teamArrayAdapter);
        lvTeamsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                launchActivity(
                        TeamHubActivity.class,
                        teamArrayAdapter.getItem(pos).getObjectId(),
                        teamArrayAdapter.getItem(pos).getName()
                );
            }
        });
    }

    private void launchActivity(Class klass, String teamId, String teamName) {
        Intent intent = new Intent(this, klass);
        intent.putExtra("teamId", teamId);
        intent.putExtra("teamName", teamName);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        teamArrayAdapter.clear(); teamArrayAdapter.notifyDataSetChanged();
        relativeLayout.removeView(txtViewNoPrograms);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListView();
        fetchCurrentAthleteTeamInfo();
    }

    private void fetchCurrentAthleteTeamInfo() {
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
            if (fetcher.teamList.size() > 0) {
                addToArrayAdapter();
            } else {
                addTextView();
            }
        }

        private void addToArrayAdapter() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (ParseObject teamParseObj : fetcher.teamList) {
                        teamArrayAdapter.add(new Team(
                                teamParseObj.getObjectId(),
                                teamParseObj.getString("name")
                            )
                        );
                    }
                    teamArrayAdapter.notifyDataSetChanged();
                }
            });
        }

        private void addTextView() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtViewNoPrograms = new TextView(applicationContext);
                    txtViewNoPrograms.setText("You're not currently a member of any teams.\nCreate a new team!");
                    txtViewNoPrograms.setTextSize(16);
                    txtViewNoPrograms.setLineSpacing(10, 1);
                    txtViewNoPrograms.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
                    txtViewNoPrograms.setGravity(Gravity.CENTER_HORIZONTAL);
                    txtViewNoPrograms.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    relativeLayout.addView(txtViewNoPrograms);
                }
            });
        }
    }
}

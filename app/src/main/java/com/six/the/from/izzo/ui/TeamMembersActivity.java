package com.six.the.from.izzo.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.ParseObject;
import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.Athlete;
import com.six.the.from.izzo.util.ParseUtils;
import com.six.the.from.izzo.util.TeamFetcher;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActionBarActivity;

public class TeamMembersActivity extends RoboActionBarActivity {
    ParseObject teamParseObject;
    ImageView bmpImageView;
    ArrayAdapter<String> teamMembersArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_members);
        initViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Team Members");
        if (!getIntent().getStringExtra("iconImageUrl").isEmpty()) {
            Picasso.with(this.getApplicationContext()).load(getIntent().getStringExtra("iconImageUrl")).into(bmpImageView);
        }
        new FetchTeamThread().start();
    }

    private void initViews() {
        bmpImageView = (ImageView) findViewById(R.id.img_team_logo);
        ListView listView = (ListView) findViewById(R.id.lv_team_members);
        teamMembersArrayAdapter = new ArrayAdapter<>(this,
                R.layout.simple_custom_list_item);
        listView.setAdapter(teamMembersArrayAdapter);
    }

    private class FetchTeamThread extends Thread {
        private final TeamFetcher fetcher = new TeamFetcher();

        public FetchTeamThread() {
        }

        public void run() {
            ParseUtils.fetchTeam(fetcher, getIntent().getStringExtra("teamId"));

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
                    if (!fetcher.fetching) {
                        teamParseObject = fetcher.teamParseObj;
                        new FetchTeamMembersThread().start();
                    }
                }
            });
        }
    }

    private class FetchTeamMembersThread extends Thread {
        private final TeamMembersFetcher teamMembersFetcher = new TeamMembersFetcher();

        public FetchTeamMembersThread() {
        }

        public void run() {
            ParseUtils.fetchTeamMembers(teamMembersFetcher, teamParseObject);

            while (teamMembersFetcher.fetching) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!teamMembersFetcher.fetching) {
                        for (int i = 0; i < teamMembersFetcher.teamMembers.size(); i++) {
                            Athlete athlete = teamMembersFetcher.teamMembers.get(i);
                            String athleteName = athlete.getFirstName();
                            if (athlete.getLastName() != null && !athlete.getLastName().isEmpty()) {
                                athleteName += " " + athlete.getLastName();
                            }
                            teamMembersArrayAdapter.add(athleteName);
                        }
                        teamMembersArrayAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public class TeamMembersFetcher {
        public volatile boolean fetching;
        public volatile List<Athlete> teamMembers = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team_members, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_settings:
                break;
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

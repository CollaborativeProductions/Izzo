package com.six.the.from.izzo.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseObject;
import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.Athlete;
import com.six.the.from.izzo.util.ParseUtils;
import com.six.the.from.izzo.util.TeamFetcher;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActionBarActivity;

public class TeamMembersActivity extends RoboActionBarActivity {
    LinearLayout linearLayout;
    ParseObject teamParseObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_members);
        linearLayout = (LinearLayout) findViewById(R.id.activity_team_members);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (!getIntent().getStringExtra("teamName").isEmpty()) {
            getSupportActionBar().setTitle(getIntent().getStringExtra("teamName"));  // provide compatibility to all the versions
        }
        new FetchTeamThread(this.getApplicationContext()).start();
    }

    private class FetchTeamThread extends Thread {
        private final TeamFetcher fetcher = new TeamFetcher();
        private Context applicationContext;

        public FetchTeamThread(Context context) {
            this.applicationContext = context;
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
                        new FetchTeamMembersThread(applicationContext).start();
                    }
                }
            });
        }
    }

    private class FetchTeamMembersThread extends Thread {
        private final TeamMembersFetcher teamMembersFetcher = new TeamMembersFetcher();
        private Context applicationContext;

        public FetchTeamMembersThread(Context context) {
            this.applicationContext = context;
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
                        View view;
                        TextView txtViewTeamMemberName;
                        for (int i = 0; i < teamMembersFetcher.teamMembers.size(); i++) {
                            view = View.inflate(applicationContext, R.layout.team_member_list_item, null);
                            txtViewTeamMemberName = (TextView) view.findViewById(R.id.txt_team_member_name);
                            txtViewTeamMemberName.setText(teamMembersFetcher.teamMembers.get(i).getFirstName() + " " + teamMembersFetcher.teamMembers.get(i).getLastName());
                            if (i != 0) {
                                view.setBackground(applicationContext.getResources().getDrawable(R.drawable.border_top));
                            }
                            linearLayout.addView(view);
                        }
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

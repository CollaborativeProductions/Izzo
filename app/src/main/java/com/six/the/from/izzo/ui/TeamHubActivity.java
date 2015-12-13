package com.six.the.from.izzo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.six.the.from.izzo.R;
import com.six.the.from.izzo.util.ParseUtils;
import com.six.the.from.izzo.util.TeamFetcher;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActionBarActivity;


public class TeamHubActivity extends RoboActionBarActivity {
    ImageView bmpImageView;
    ParseObject teamParseObject;
    Context applicationContext;
    List<ParseObject> teamPrograms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationContext = this.getApplicationContext();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().hasExtra("teamName")) {
            getSupportActionBar().setTitle(getIntent().getStringExtra("teamName"));  // provide compatibility to all the versions
        }
        setContentView(R.layout.activity_team_hub);

        initViews();
        new FetchTeamThread().start();
        new FetchTeamProgramsThread().start();
    }

    private void initViews() {
        bmpImageView = (ImageView) findViewById(R.id.img_team_logo);
        ListView listView = (ListView) findViewById(R.id.lv_team_menu);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        arrayAdapter.add("Current Program");
        arrayAdapter.add("Team Members");
        arrayAdapter.add("Statistics");
        arrayAdapter.add("All Programs");
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                switch (arrayAdapter.getItem(pos)) {
                    case "Current Program":
                        launchCurrentProgramActivity();
                        break;
                    case "Team Members":
                        launchTeamMembersActivity();
                        break;
                    case "Statistics":
//                        Intent intent = new Intent(TeamHubActivity.this, StatisticsActivity.class);
//                        startActivity(intent);
                        break;
                    case "Previous Programs":
//                        Intent intent = new Intent(TeamHubActivity.this, PreviousProgramsActivity.class);
//                        startActivity(intent);
                        break;

                }
            }
        });;
    }

    private class FetchTeamThread extends Thread {
        private final TeamFetcher fetcher = new TeamFetcher();

        public FetchTeamThread() { }

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
                        ParseFile pFile = teamParseObject.getParseFile("iconImageUrl");
                        String iconUrl = pFile.getUrl();
                        if (!iconUrl.isEmpty()) {
                            Picasso.with(applicationContext).load(iconUrl).into(bmpImageView);
                        }
                    }
                }
            });
        }
    }

    private class FetchTeamProgramsThread extends Thread {
        private final FetchTeamProgramsStatusFetcher fetcher = new FetchTeamProgramsStatusFetcher();

        public FetchTeamProgramsThread() { }

        public void run() {
            ParseUtils.fetchTeamPrograms(fetcher, getIntent().getStringExtra("teamId"));

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
                    if (!fetcher.fetching && fetcher.programParseObjs.size() > 0) {
                        teamPrograms = new ArrayList<>(fetcher.programParseObjs);
                        if (getIntent().hasExtra("caller") && getIntent().getStringExtra("caller").equals("NewProgramDetails")) {
                            getIntent().removeExtra("caller");
                            launchCurrentProgramActivity();
                        }
                    }
                }
            });
        }
    }

    public class FetchTeamProgramsStatusFetcher {
        public volatile boolean fetching;
        public List<ParseObject> programParseObjs = new ArrayList<>();
    }

    private void launchCurrentProgramActivity() {
        ParseObject currentProgram = teamPrograms.get(0);
        Intent intent = new Intent(TeamHubActivity.this, CurrentProgramActivity.class);
        intent.putExtra("programId", currentProgram.getObjectId());
        intent.putExtra("programName", currentProgram.getString("name"));
        String iconUrl = currentProgram.getParseFile("iconImageUrl").getUrl();
        intent.putExtra("iconImageUrl", iconUrl);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    private void launchTeamMembersActivity() {
        Intent intent = new Intent(TeamHubActivity.this, TeamMembersActivity.class);
        intent.putExtra("teamId", getIntent().getStringExtra("teamId"));
        intent.putExtra("teamName", getIntent().getStringExtra("teamName"));
        String iconUrl = teamParseObject.getParseFile("iconImageUrl").getUrl();
        intent.putExtra("iconImageUrl", iconUrl);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
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
        switch(item.getItemId()) {
            case R.id.action_settings:
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

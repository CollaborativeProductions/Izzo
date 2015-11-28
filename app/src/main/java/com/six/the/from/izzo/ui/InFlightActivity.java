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
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.six.the.from.izzo.R;
import com.six.the.from.izzo.util.ParseUtils;
import com.six.the.from.izzo.util.TeamFetcher;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActionBarActivity;


public class InFlightActivity extends RoboActionBarActivity {
    ImageView bmpImageView;
    ParseObject teamParseObject;
    Context applicationContext;
    List<ParseObject> teamPrograms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationContext = this.getApplicationContext();

        if (getIntent().hasExtra("caller") && getIntent().getStringExtra("caller").equals("NewProgramDetails")) {
            Intent intent;
            intent = new Intent(this, CurrentProgramActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().hasExtra("teamName")) {
            getSupportActionBar().setTitle(getIntent().getStringExtra("teamName"));  // provide compatibility to all the versions
        }
        setContentView(R.layout.activity_in_flight);

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
                Intent intent;
                String iconUrl;
                switch (arrayAdapter.getItem(pos)) {
                    case "Current Program":
                        intent = new Intent(InFlightActivity.this, CurrentProgramActivity.class);
                        intent.putExtra("programId", teamPrograms.get(0).getString("objectId"));
                        intent.putExtra("programName", teamPrograms.get(0).getString("name"));
                        iconUrl = teamPrograms.get(0).getParseFile("iconImageUrl").getUrl();
                        intent.putExtra("iconImageUrl", iconUrl);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                    case "Team Members":
                        intent = new Intent(InFlightActivity.this, TeamMembersActivity.class);
                        intent.putExtra("teamId", getIntent().getStringExtra("teamId"));
                        intent.putExtra("teamName", getIntent().getStringExtra("teamName"));
                        iconUrl = teamParseObject.getParseFile("iconImageUrl").getUrl();
                        intent.putExtra("iconImageUrl", iconUrl);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                    case "Statistics":
//                        Intent intent = new Intent(InFlightActivity.this, StatisticsActivity.class);
//                        startActivity(intent);
                        break;
                    case "Previous Programs":
//                        Intent intent = new Intent(InFlightActivity.this, PreviousProgramsActivity.class);
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
                        Toast.makeText(applicationContext, "Ready to rumble with..." + fetcher.programParseObjs.get(0).getString("name"), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public class FetchTeamProgramsStatusFetcher {
        public volatile boolean fetching;
        public List<ParseObject> programParseObjs = new ArrayList<>();
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

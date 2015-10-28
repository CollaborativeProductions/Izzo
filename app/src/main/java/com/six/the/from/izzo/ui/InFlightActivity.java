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

import roboguice.activity.RoboActionBarActivity;


public class InFlightActivity extends RoboActionBarActivity {
    ImageView bmpImageView;
    ParseObject teamParseObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (!getIntent().getStringExtra("teamName").isEmpty()) {
            getSupportActionBar().setTitle(getIntent().getStringExtra("teamName"));  // provide compatibility to all the versions
        }
        setContentView(R.layout.activity_in_flight);
        initViews();
        new FetchTeamThread(this.getApplicationContext()).start();
    }

    private void initViews() {
        bmpImageView = (ImageView) findViewById(R.id.img_team_logo);
        ListView listView = (ListView) findViewById(R.id.lv_team_menu);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        arrayAdapter.add("Current Program");
        arrayAdapter.add("Team Members");
        arrayAdapter.add("Statistics");
        arrayAdapter.add("Previous Programs");
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                switch (arrayAdapter.getItem(pos)) {
                    case "Current Program":
//                        Intent intent = new Intent(InFlightActivity.this, CurrentProgramActivity.class);
//                        startActivity(intent);
                        break;
                    case "Team Members":
                        Intent intent = new Intent(InFlightActivity.this, TeamMembersActivity.class);
                        intent.putExtra("teamId", getIntent().getStringExtra("teamId"));
                        intent.putExtra("teamName", getIntent().getStringExtra("teamName"));
                        String iconUrl = teamParseObject.getParseFile("iconImageUrl").getUrl();
                        intent.putExtra("iconImageUrl", iconUrl);
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

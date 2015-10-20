package com.six.the.from.izzo.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.CurrentAthlete;
import com.six.the.from.izzo.util.ParseUtils;
import com.six.the.from.izzo.util.TeamFetcher;

import java.io.InputStream;

import javax.inject.Inject;

import roboguice.activity.RoboActionBarActivity;


public class InFlightActivity extends RoboActionBarActivity {
    @Inject
    CurrentAthlete currentAthlete;
    ImageView bmpImageView;
    TextView teamNameTxtView;
    ParseObject teamParseObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_flight);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!getIntent().hasExtra("teamId")) return;

        initViews();
        new FetchTeamThread().start();
    }

    private void initViews() {
        bmpImageView = (ImageView) findViewById(R.id.img_team_logo);
        teamNameTxtView = (TextView) findViewById(R.id.txt_team_name);
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
                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("teamParseSerializableObject", new ParseSerializableObject(teamParseObject));
                        Intent intent = new Intent(InFlightActivity.this, TeamMembersActivity.class);
                        intent.putExtra("teamId", teamParseObject.getObjectId());
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        public DownloadImageTask() { }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmpImageView.setImageBitmap(result);
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), ((BitmapDrawable) bmpImageView.getDrawable()).getBitmap());
            drawable.setCornerRadius(Math.min(bmpImageView.getMinimumWidth(), bmpImageView.getMinimumHeight()));
        }
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
                        ParseFile pFile = fetcher.teamParseObj.getParseFile("iconImageUrl");
                        String iconUrl = pFile.getUrl();
                        if (!iconUrl.isEmpty()) {
                            teamNameTxtView.setText(fetcher.teamParseObj.getString("name"));
                            new DownloadImageTask().execute(iconUrl);
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

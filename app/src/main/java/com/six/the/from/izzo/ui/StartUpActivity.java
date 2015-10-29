package com.six.the.from.izzo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseObject;
import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.CurrentAthlete;
import com.six.the.from.izzo.util.ParseUtils;

import javax.inject.Inject;

import roboguice.activity.RoboActionBarActivity;

public class StartUpActivity extends RoboActionBarActivity {
    @Inject
    CurrentAthlete currentAthlete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        initParse();
        fetchCurrentAthleteInfo();
    }

    private void initParse() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "uiZSGIayyGxGlMGzzlv5PtoS8yJtEyplxVqxHALN", "06h7dcf88vcFFdVCrtBsGyMQ70CYU8C7dacfDcN6");
    }

    private void fetchCurrentAthleteInfo() {
        new FetchCurrentAthleteThread().start();
    }

    private class FetchCurrentAthleteThread extends Thread {
        private final CurrentAthleteFetcher fetcher = new CurrentAthleteFetcher();

        public FetchCurrentAthleteThread() { }

        public void run() {
            TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            ParseUtils.uuidPhoneNumberExists(fetcher, tManager.getLine1Number(), tManager.getDeviceId());

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
                    if (fetcher.athlete == null) {
                        launchActivity(RegistrationActivity.class);
                    } else {
                        currentAthlete.setParseObject(fetcher.athlete);
                        launchActivity(CustomTabsActivity.class);
                    }
                }
            });
        }
    }

    private void launchActivity(Class klass) {
        Intent intent = new Intent(this.getApplication(), klass);
        // TODO: Check for in-flight activity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_up, menu);
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

    public static class CurrentAthleteFetcher {
        public volatile boolean fetching;
        public volatile ParseObject athlete;
    }
}

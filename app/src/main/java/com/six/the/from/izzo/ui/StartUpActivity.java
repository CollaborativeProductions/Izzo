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

    private final Heartbeat heartbeat = new Heartbeat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        initParse();

        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        ParseUtils.uuidPhoneNumberExists(heartbeat, tManager.getLine1Number(), tManager.getDeviceId());

        new FetchAthleteInfo().start();
    }

    public void initParse() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "uiZSGIayyGxGlMGzzlv5PtoS8yJtEyplxVqxHALN", "06h7dcf88vcFFdVCrtBsGyMQ70CYU8C7dacfDcN6");
    }

    private class FetchAthleteInfo extends Thread {
        private boolean readingFromParse = true;

        public FetchAthleteInfo() { }

        public void run() {
            while (readingFromParse) {
                if (heartbeat.done) {
                    currentAthlete.setObjectId(heartbeat.objectId);
                    currentAthlete.setUuid(heartbeat.uuid);
                    currentAthlete.setFirstName(heartbeat.firstName);
                    currentAthlete.setLastName(heartbeat.lastName);
                    currentAthlete.setPhoneNumber(heartbeat.phoneNumber);
                    currentAthlete.setParseObject(heartbeat.athlete);
                    readingFromParse = false;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (heartbeat.exists) {
                        launchActivity(CustomTabsActivity.class);
                    } else {
                        launchActivity(RegistrationActivity.class);
                    }
                }
            });
        }
    }

    private void launchActivity(Class klass) {
        Intent intent = new Intent(this.getApplication(), klass);
        // TODO: Check for in-flight activity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public static class Heartbeat {
        public boolean exists;
        public boolean done;
        public String objectId;
        public String uuid;
        public String firstName;
        public String lastName;
        public String phoneNumber;
        public ParseObject athlete;
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
}

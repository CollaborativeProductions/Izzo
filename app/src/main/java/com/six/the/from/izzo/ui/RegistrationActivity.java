package com.six.the.from.izzo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.ParseObject;
import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.CurrentAthlete;
import com.six.the.from.izzo.util.IzzoEditText;
import com.six.the.from.izzo.util.ParseUtils;

import javax.inject.Inject;

import roboguice.activity.RoboActionBarActivity;

public class RegistrationActivity extends RoboActionBarActivity {
    @Inject
    CurrentAthlete currentAthlete;

    private IzzoEditText txtPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initView();
    }

    private void initView() {
        txtPhoneNumber = (IzzoEditText) findViewById(R.id.txt_phone_number);
        Button btnGetStarted = (Button) findViewById(R.id.btn_get_started);
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            public void onClick(View v) {
                ParseObject newAthlete = ParseUtils.saveAthlete(
                        tManager.getDeviceId(),
                        "first name",   //TODO: ? maybe on another activity
                        "last name",    //TODO: ? maybe on another activity
                        txtPhoneNumber.getText().toString(),
                        currentAthlete.getParseObject()
                );
                currentAthlete.setParseObject(newAthlete);
                launchActivity(CustomTabsActivity.class);
            }
        });
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
        getMenuInflater().inflate(R.menu.menu_registration, menu);
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

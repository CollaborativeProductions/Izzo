package com.six.the.from.izzo;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

import com.parse.Parse;

public class CustomTabsActivity extends TabActivity {
    private TabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_host);
        setTabs();
        initParse();
    }

    private void initParse() {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "uiZSGIayyGxGlMGzzlv5PtoS8yJtEyplxVqxHALN", "06h7dcf88vcFFdVCrtBsGyMQ70CYU8C7dacfDcN6");
    }

    private void setTabs() {
        tabHost = getTabHost();

        addFakeTab(R.string.tab_0);
        addTab(R.string.tab_1, R.drawable.tab_home);
        addTab(R.string.tab_2, R.drawable.tab_team);
        addTab(R.string.tab_3, R.drawable.tab_single);

        tabHost.setCurrentTab(0);
    }

    private void addFakeTab(int labelId) {
        Intent intent = new Intent(this, InFlightActivity.class);
        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator_fake, getTabWidget(), false);

        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);
    }

    private void addTab(int labelId, int drawableId) {
        Intent intent;
        switch (labelId) {
            case R.string.tab_1:
                intent = new Intent(this, ProgramsActivity.class);
                break;
            case R.string.tab_2:
                intent = new Intent(this, NewTeamActivity.class);
                break;
            case R.string.tab_3:
                intent = new Intent(this, NewWorkoutActivity.class);
                break;
            default:
                intent = new Intent(this, ProgramsActivity.class);
                break;
        }
        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);

        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);

        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);
    }
}
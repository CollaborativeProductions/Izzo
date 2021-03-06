package com.six.the.from.izzo.ui;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

import com.six.the.from.izzo.R;

public class CustomTabsActivity extends TabActivity {
    public TabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_host);
        tabHost = getTabHost();
//        addFakeTab(this.getApplicationContext(), R.string.tab_0);
        setTabs(this.getApplicationContext());
    }

    public void setTabs(Context ApplicationContext) {
        addTab(ApplicationContext, R.string.tab_1, R.drawable.tab_home);
        addTab(ApplicationContext, R.string.tab_2, R.drawable.tab_team);
        addTab(ApplicationContext, R.string.tab_3, R.drawable.tab_single);

        tabHost.setCurrentTab(0);
    }

    private void addFakeTab(Context ApplicationContext, int labelId) {
        Intent intent = new Intent(ApplicationContext, TeamHubActivity.class);

        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(ApplicationContext).inflate(R.layout.tab_indicator_fake, tabHost.getTabWidget(), false);

        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);
    }

    private void addTab(Context ApplicationContext, int labelId, int drawableId) {
        Intent intent;
        switch (labelId) {
            case R.string.tab_1:
                intent = new Intent(ApplicationContext, AllProgramsActivity.class);
                break;
            case R.string.tab_2:
                intent = new Intent(ApplicationContext, NewTeamActivity.class);
                break;
            case R.string.tab_3:
                intent = new Intent(ApplicationContext, NewProgramActivity.class);
                break;
            default:
                intent = new Intent(ApplicationContext, AllProgramsActivity.class);
                break;
        }
        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(ApplicationContext).inflate(R.layout.tab_indicator, tabHost.getTabWidget(), false);

        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);

        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);
    }

    public void clearAllTabs() {
        tabHost.clearAllTabs();
    }


    private void checkForInFlightActivity() {
//        TODO: Check local parse database for in-flight activity
    }
}
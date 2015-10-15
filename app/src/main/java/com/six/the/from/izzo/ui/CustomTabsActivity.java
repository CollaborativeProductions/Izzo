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

import roboguice.activity.RoboTabActivity;

public class CustomTabsActivity extends RoboTabActivity {
    public static TabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_host);
        tabHost = getTabHost();
        addFakeTab(this.getApplicationContext(), R.string.tab_0);
        setTabs(this.getApplicationContext());
    }

    public static void setTabs(Context ApplicationContext) {
        addTab(ApplicationContext, R.string.tab_1, R.drawable.tab_home);
        addTab(ApplicationContext, R.string.tab_2, R.drawable.tab_team);
        addTab(ApplicationContext, R.string.tab_3, R.drawable.tab_single);

        tabHost.setCurrentTab(1);
    }

    private static void addFakeTab(Context ApplicationContext, int labelId) {
        Intent intent = new Intent(ApplicationContext, InFlightActivity.class);

        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(ApplicationContext).inflate(R.layout.tab_indicator_fake, tabHost.getTabWidget(), false);

        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);
    }

    private static void addTab(Context ApplicationContext, int labelId, int drawableId) {
        Intent intent;
        switch (labelId) {
            case R.string.tab_1:
                intent = new Intent(ApplicationContext, ProgramsActivity.class);
                break;
            case R.string.tab_2:
                intent = new Intent(ApplicationContext, NewTeamActivity.class);
                break;
            case R.string.tab_3:
                intent = new Intent(ApplicationContext, NewWorkoutActivity.class);
                break;
            default:
                intent = new Intent(ApplicationContext, ProgramsActivity.class);
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
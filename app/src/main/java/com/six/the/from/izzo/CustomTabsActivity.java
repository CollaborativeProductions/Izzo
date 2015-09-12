package com.six.the.from.izzo;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

public class CustomTabsActivity extends TabActivity {
    private TabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_host);
        setTabs();
    }

    private void setTabs() {
        tabHost = getTabHost();

        addTab(R.string.tab_1, R.drawable.tab_main);
        addTab(R.string.tab_2, R.drawable.tab_main);
        addTab(R.string.tab_3, R.drawable.tab_main);
        addTab(R.string.tab_4, R.drawable.tab_main);
    }

    private void addTab(int labelId, int drawableId) {
        Intent intent = new Intent(this, ShareActivity.class);
        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);

        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);

        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);

    }
}
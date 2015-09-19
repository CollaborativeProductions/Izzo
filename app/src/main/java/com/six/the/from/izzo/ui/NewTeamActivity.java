package com.six.the.from.izzo.ui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;

import com.six.the.from.izzo.util.IzzoEditText;
import com.six.the.from.izzo.R;
import com.six.the.from.izzo.util.Validation;


public class NewTeamActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_team);
        initTextView();
    }

    private void initTextView() {
        final IzzoEditText etViewTeamName = (IzzoEditText) findViewById(R.id.et_new_team_name);
        etViewTeamName.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) etViewTeamName.setError(null);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        IzzoEditText etViewTeamName = (IzzoEditText) findViewById(R.id.et_new_team_name);
        etViewTeamName.setText("");
        etViewTeamName.setError(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_team, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_next:
                IzzoEditText etViewTeamName = (IzzoEditText) findViewById(R.id.et_new_team_name);
                if (Validation.hasText(etViewTeamName)) {
                    Intent intent = new Intent(this, ContactsListActivity.class);
                    intent.putExtra("teamName", etViewTeamName.getText().toString());
                    startActivity(intent);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

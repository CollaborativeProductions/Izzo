package com.six.the.from.izzo.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.six.the.from.izzo.util.ContactArrayAdapter;
import com.six.the.from.izzo.util.IzzoEditText;
import com.six.the.from.izzo.util.ParseUtils;
import com.six.the.from.izzo.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;


public class ContactsListActivity extends ActionBarActivity {
    private String searchString;
    private Cursor cur;
    private SimpleCursorAdapter scAdapter;
    private ContactArrayAdapter contactArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Team Members");

        initSelectedListView();
        initSearchListView();
        setEditTextChangedListener();
    }

    private void initSelectedListView() {
        ListView lvSelectedList = (ListView) findViewById(R.id.selected_list_view);
        contactArrayAdapter = new ContactArrayAdapter(this, R.layout.list_item_contact);
        lvSelectedList.setAdapter(contactArrayAdapter);
        lvSelectedList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                contactArrayAdapter.remove(pos);
                contactArrayAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void initSearchListView() {
        cur = getContacts();
        String[] from_fields = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        int[] to_fields = new int[] {
                R.id.name_entry,
                R.id.number_entry
        };
        ListView lvContactSearch = (ListView) findViewById(R.id.contacts_list_view);
        scAdapter =
                new SimpleCursorAdapter(this,
                        R.layout.list_item_contact,
                        cur,
                        from_fields,
                        to_fields,
                        0);
        lvContactSearch.setAdapter(scAdapter);
        lvContactSearch.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                TextView name = (TextView) view.findViewById(R.id.name_entry);
                TextView phoneNumber = (TextView) view.findViewById(R.id.number_entry);
                contactArrayAdapter.add(name.getText().toString(), phoneNumber.getText().toString());
                contactArrayAdapter.notifyDataSetChanged();
                resetSearchStringField();
            }
        });
    }

    private void setEditTextChangedListener() {
        EditText etSearchString = (EditText) findViewById(R.id.et_search_string);
        etSearchString.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable text) {
            }

            public void beforeTextChanged(CharSequence text, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence text, int start,
                                      int before, int count) {
                setSearchString(text.toString());
                updateCursor(getContacts());
            }
        });
    }

    private void updateCursor(Cursor swapCursor) {
        scAdapter.swapCursor(swapCursor);
    }

    private Cursor getContacts() {
        // Run query
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection =
                new String[] {
                        ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };
        String selection =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?" :
                        ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?";
        String[] selectionArgs = { "%" + getSearchString() + "%" };
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME +
                " COLLATE LOCALIZED ASC";
        return getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    private void resetSearchStringField() {
        IzzoEditText etSearchText = (IzzoEditText) findViewById(R.id.et_search_string);
        etSearchText.setText("");
    };

    private void setSearchString(String text) {
        searchString = (text.length() == 0) ? null : text;
    }

    private String getSearchString() {
        return searchString;
    }

    private void saveTeam() {
        new SaveTeamThread(this.getApplicationContext()).start();
    }

    private class SaveTeamThread extends Thread {
        private final OperationStatusFetcher fetcher = new OperationStatusFetcher();
        private final Context applicationContext;

        public SaveTeamThread(Context applicationContext) {
            this.applicationContext = applicationContext;
        }

        public void run() {
            ParseFile parseFile = null;
            if (getIntent().hasExtra("imageFile")) {
                try {
                    Bitmap bmpImage = BitmapFactory.decodeStream(applicationContext.openFileInput("izzoTeamIconImage"));
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    bmpImage.compress(Bitmap.CompressFormat.PNG, 100, bs);
                    parseFile = new ParseFile("imageData.txt", bs.toByteArray());
                    parseFile.saveInBackground();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (getIntent().hasExtra("teamId")) {
                ParseUtils.updateTeam(contactArrayAdapter, getIntent().getStringExtra("teamId"), fetcher);
            } else {
                ParseUtils.saveTeam(contactArrayAdapter, getIntent().getStringExtra("teamName"), tManager.getDeviceId(), parseFile, fetcher);
            }

            while (fetcher.saving) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!fetcher.saving) {
                        launchActivity(TeamHubActivity.class, fetcher.teamParseObj.getObjectId(), fetcher.teamParseObj.getString("name"));
                    }
                }
            });
        }
    }

    private void launchActivity(Class klass, String teamId, String teamName) {
        Intent intent = new Intent(this, klass);
        intent.putExtra("teamId", teamId);
        intent.putExtra("teamName", teamName);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public class OperationStatusFetcher {
        public volatile boolean saving;
        public volatile ParseObject teamParseObj;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_done:
                if (contactArrayAdapter.isEmpty()) {
                    IzzoEditText etSearchText = (IzzoEditText) findViewById(R.id.et_search_string);
                    etSearchText.setError("");
                    return false;
                }
                saveTeam();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

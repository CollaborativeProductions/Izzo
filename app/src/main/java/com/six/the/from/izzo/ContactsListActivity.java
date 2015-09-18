package com.six.the.from.izzo;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import android.widget.Toast;


public class ContactsListActivity extends ActionBarActivity {
    private String searchString;
    private Cursor cur;
    private SimpleCursorAdapter scAdapter;
    private ContactArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initSelectedListView();
        initSearchListView();
        setEditTextChangedListener();
    }

    public void initSelectedListView() {
        ListView lvSelectedList = (ListView) findViewById(R.id.member_list_view);
        arrayAdapter = new ContactArrayAdapter(this, R.layout.contact_list_item);
        lvSelectedList.setAdapter(arrayAdapter);
        lvSelectedList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                arrayAdapter.remove(pos);
                arrayAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    public void initSearchListView() {
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
                        R.layout.contact_list_item,
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
                arrayAdapter.add(name.getText().toString(), phoneNumber.getText().toString());
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Added " + name.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setEditTextChangedListener() {
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

    private void setSearchString(String text) {
        searchString = (text.length() == 0) ? null : text;
    }

    private String getSearchString() {
        return searchString;
    }

    public void saveTeam() {
        for (int pos = 0; pos < arrayAdapter.getCount(); pos++) {
            arrayAdapter.saveInBackground(pos);
        }
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
                saveTeam();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
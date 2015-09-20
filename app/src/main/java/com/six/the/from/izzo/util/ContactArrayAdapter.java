package com.six.the.from.izzo.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.six.the.from.izzo.R;

import java.util.ArrayList;
import java.util.HashMap;


public class ContactArrayAdapter extends ArrayAdapter<ContactListItem> {
    private HashMap<String, String> contactMap;

    public ContactArrayAdapter(Context context, int resource) {
        super(context, resource);
        this.contactMap = new HashMap<>();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Get the data item for this position
        ContactListItem contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.contact_list_item, parent, false);
        }
        // Lookup view for data population
        TextView txtNameEntry = (TextView) view.findViewById(R.id.name_entry);
        TextView txtNumberEntry = (TextView) view.findViewById(R.id.number_entry);

        // Populate the data into the template view using the data object
        txtNameEntry.setText(contact.getName());
        txtNumberEntry.setText(contact.getPhoneNumber());

        // Return the completed view to render on screen
        return view;
    }

    public void add(String name, String phoneNumber) {
        if (this.contactMap.containsKey(phoneNumber) &&
                this.contactMap.get(phoneNumber).equals(name))
            return;
        super.add(new ContactListItem(name, phoneNumber));
        this.contactMap.put(phoneNumber, name);
    }

    public void remove(int position) {
        ContactListItem contact = getItem(position);
        super.remove(contact);
        this.contactMap.remove(contact.getPhoneNumber());
    }
}
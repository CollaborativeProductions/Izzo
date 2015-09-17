package com.six.the.from.izzo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactArrayAdapter extends ArrayAdapter<ContactListItem> {
    public ContactArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ContactArrayAdapter(Context context, int resource, ArrayList<ContactListItem> contacts) {
        super(context, resource, contacts);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Get the data item for this position
        ContactListItem contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.contact_list_item, parent, false);
//            view = LayoutInflater.from(getContext()).inflate(R.layout.contact_list_item, parent, false);
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
}

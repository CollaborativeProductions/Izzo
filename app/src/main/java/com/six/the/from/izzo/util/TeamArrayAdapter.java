package com.six.the.from.izzo.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.six.the.from.izzo.R;
import com.six.the.from.izzo.models.Team;

import java.util.HashMap;


public class TeamArrayAdapter extends ArrayAdapter<Team> {
    private HashMap<String, Team> teamsMap = new HashMap<>();

    public TeamArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Get the data item for this position
        Team team = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_team, parent, false);
        }
        // Lookup view for data population
        TextView txtTeamName = (TextView) view.findViewById(R.id.team_name);

        // Populate the data into the template view using the data object
        txtTeamName.setText(team.getName());

        // Return the completed view to render on screen
        return view;
    }

    public void add(Team team) {
        if (this.contains(team.getObjectId()))
            return;
        super.add(team);
        this.teamsMap.put(team.getObjectId(), team);
    }

    public void remove(int position) {
        Team team = getItem(position);
        super.remove(team);
        this.teamsMap.remove(team.getObjectId());
    }

    public boolean contains(String objectId) {
        return teamsMap.get(objectId) != null;
    }
}

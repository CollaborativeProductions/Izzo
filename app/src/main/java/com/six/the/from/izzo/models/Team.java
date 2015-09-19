package com.six.the.from.izzo.models;


import com.parse.ParseObject;

public class Team {
    private String id;
    private String group_name;

    public Team(String id, String group_name) {
        setId(id);
        setGroupName(group_name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return group_name;
    }

    public void setGroupName(String group_name) {
        this.group_name = group_name;
    }

    public void saveInBackground() {
        ParseObject team = new ParseObject("Team");
        team.put("name", this.getGroupName());
        team.saveInBackground();
    }
}

package com.six.the.from.izzo.models;


import com.parse.ParseObject;

public class Team {
    private String objectId;
    private String name;

    public Team(String objectId, String teamName) {
        setId(objectId);
        setName(teamName);
    }

    public String getObjectId() {
        return objectId;
    }

    public void setId(String id) {
        this.objectId = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String teamName) {
        this.name = teamName;
    }

    public void saveInBackground() {
        ParseObject team = new ParseObject("Team");
        team.put("name", this.getName());
        team.saveInBackground();
    }
}

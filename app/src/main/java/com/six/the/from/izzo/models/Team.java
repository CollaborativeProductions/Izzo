package com.six.the.from.izzo.models;


import com.parse.ParseObject;

public class Team {
    private String objectId;
    private String name;
    private String iconUrl;

    public Team(String objectId, String teamName) {
        setId(objectId);
        setName(teamName);
    }

    public Team(String teamName) {
        setName(teamName);
    }

    public Team(String objectId, String teamName, String iconUrl) {
        setId(objectId);
        setName(teamName);
        setIconUrl(iconUrl);
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

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void saveInBackground() {
        ParseObject team = new ParseObject("Team");
        team.put("name", this.getName());
        team.saveInBackground();
    }
}

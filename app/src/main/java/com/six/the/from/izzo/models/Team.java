package com.six.the.from.izzo.models;


import com.parse.ParseObject;

public class Team {
    private String id;
    private String name;

    public Team(String id, String teamName) {
        setId(id);
        setName(teamName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

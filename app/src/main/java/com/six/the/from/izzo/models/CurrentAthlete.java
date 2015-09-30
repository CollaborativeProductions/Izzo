package com.six.the.from.izzo.models;

import com.parse.ParseObject;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CurrentAthlete {
    private ParseObject athleteParseObject;

    @Inject
    public CurrentAthlete() { }

    public ParseObject getParseObject() {
        return athleteParseObject;
    }

    public void setParseObject(ParseObject athleteParseObject) {
        this.athleteParseObject = athleteParseObject;
    }
}

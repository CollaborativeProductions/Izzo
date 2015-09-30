package com.six.the.from.izzo.models;

import com.parse.ParseObject;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CurrentAthlete {
    private String objectId;
    private String uuid;
    private String first_name;
    private String last_name;
    private String phone_number;
    private ParseObject athleteParseObject;

    @Inject
    public CurrentAthlete() { }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String UUID) {
        this.uuid = UUID;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

    public ParseObject getParseObject() {
        return athleteParseObject;
    }

    public void setParseObject(ParseObject athleteParseObject) {
        this.athleteParseObject = athleteParseObject;
    }
}

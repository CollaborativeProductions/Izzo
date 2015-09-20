package com.six.the.from.izzo.models;


import com.parse.ParseObject;

public class Athlete {
    private String objectId;
    private String uuid;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public Athlete(String uuid, String firstName, String lastName, String phoneNumber) {
        setUuid(uuid);
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
    }

    public Athlete(String objectId, String uuid, String firstName, String lastName, String phoneNumber) {
        setObjectId(objectId);
        setUuid(uuid);
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
    }

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
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void saveInBackground() {
        ParseObject newAthlete = new ParseObject("Athlete");
        newAthlete.put("uuid", this.getUuid());
        newAthlete.put("firstName", this.getFirstName());
        newAthlete.put("lastName", this.getLastName());
        newAthlete.put("phoneNumber", this.getPhoneNumber());
        newAthlete.saveInBackground();
    }
}

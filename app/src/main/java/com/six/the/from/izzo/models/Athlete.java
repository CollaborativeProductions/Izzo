package com.six.the.from.izzo.models;


public class Athlete {
    private String objectId;
    private String uuid;
    private String first_name;
    private String last_name;
    private String phone_number;

    public Athlete(String objectId, String uuid, String first_name, String last_name, String phone_number) {
        setObjectId(objectId);
        setUuid(uuid);
        setFirstName(first_name);
        setLastName(last_name);
        setPhoneNumber(phone_number);
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
}

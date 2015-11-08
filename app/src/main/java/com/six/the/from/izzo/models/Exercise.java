package com.six.the.from.izzo.models;


public class Exercise {
    private String name;
    private String type;
    private int duration;
    private int distance;
    private int[] sets;

    public Exercise(String name, int distance, int duration) {
        this.name = name;
        this.type = "Cardio";
        this.duration = duration;
        this.distance = distance;
    }

    public Exercise(String name, int[] sets) {
        this.name = name;
        this.type = "Weight";
        this.sets = sets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int[] getSets() {
        return sets;
    }

    public void setSets(int[] sets) {
        this.sets = sets;
    }
}

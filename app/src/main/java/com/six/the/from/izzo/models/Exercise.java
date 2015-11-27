package com.six.the.from.izzo.models;


public class Exercise {
    private String name;
    private String type;
    private int duration;
    private int distance;
    private int[] reps;
    private int[] weight;

    public Exercise(String name, int distance, int duration) {
        this.name = name;
        this.type = "Cardio";
        this.duration = duration;
        this.distance = distance;
    }

    public Exercise(String name, int[] reps, int[] weight) {
        this.name = name;
        this.type = "Weight";
        this.reps = reps;
        this.weight = weight;
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

    public int getNumSets() {
        return reps.length;
    }
    public int[] getNumReps() {
        return reps;
    }

    public void setNumReps(int[] reps) {
        this.reps = reps;
    }

    public int[] getWeight() {
        return weight;
    }

    public void setWeight(int[] weight) {
        this.weight = weight;
    }
}

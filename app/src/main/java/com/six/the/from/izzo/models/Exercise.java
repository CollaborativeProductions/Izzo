package com.six.the.from.izzo.models;


import com.six.the.from.izzo.util.Tuple;

public class Exercise {
    private String name;
    private String type;
    private int duration;
    private int distance;
    private Tuple<Integer, Integer>[] sets;
    private int reps;
    private int weight;

    public Exercise(String name, int distance, int duration) {
        this.name = name;
        this.type = "Cardio";
        this.duration = duration;
        this.distance = distance;
    }

    public Exercise(String name, Tuple<Integer, Integer>[] sets) {
        this.name = name;
        this.type = "Weight";
        this.sets = sets;
        this.reps = sets[0].getLeft();
        this.weight = sets[0].getRight();
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
        return sets.length;
    }

    public void setNumSets(Tuple<Integer, Integer>[] sets) {
        this.sets = sets;
    }

    public int getNumReps() {
        return reps;
    }

    public void setNumReps(int reps) {
        this.reps = reps;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

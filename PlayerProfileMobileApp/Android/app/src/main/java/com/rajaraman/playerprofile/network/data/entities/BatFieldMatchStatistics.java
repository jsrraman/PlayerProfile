// Created by rajaraman on Jan 01, 2015
package com.rajaraman.playerprofile.network.data.entities;

public class BatFieldMatchStatistics {
    private String matches;
    private String runs;
    private String highest;
    private String average;
    private String hundreds;
    private String fifties;

    public String getMatches() {
        return matches;
    }

    public void setMatches(String matches) {
        this.matches = matches;
    }

    public String getRuns() {
        return runs;
    }

    public void setRuns(String runs) {
        this.runs = runs;
    }

    public String getHighest() {
        return highest;
    }

    public void setHighest(String highest) {
        this.highest = highest;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public String getHundreds() {
        return hundreds;
    }

    public void setHundreds(String hundreds) {
        this.hundreds = hundreds;
    }

    public String getFifties() {
        return fifties;
    }

    public void setFifties(String fifties) {
        this.fifties = fifties;
    }
}
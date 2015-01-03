package com.rajaraman.playerprofile.network.data.entities;

// Created by rajaraman on Dec 25, 2014

public class PlayerEntity {
    public int countryId;
    public String playerUrl;
    public String thumbnailUrl;
    public int playerId;
    public String name;
    public String country;
    public String age;
    public String battingStyle;
    public String bowlingStyle;
    public BatFieldAvg batFieldAvg;
    public BowlAvg bowlAvg;

    public PlayerEntity(int countryId, String playerUrl, String thumbnailUrl, int playerId,
                            String name, String country, String age, String battingStyle,
                            String bowlingStyle, BatFieldAvg batFieldAvg, BowlAvg bowlAvg) {
        this.countryId = countryId;
        this.playerUrl = playerUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.playerId = playerId;
        this.name = name;
        this.country = country;
        this.age = age;
        this.battingStyle = battingStyle;
        this.bowlingStyle = bowlingStyle;
        this.batFieldAvg = batFieldAvg;
        this.bowlAvg = bowlAvg;
    }
}
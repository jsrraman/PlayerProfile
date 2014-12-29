package com.rajaraman.playerprofile.network.data.entity;

// Created by rajaraman on Dec 25, 2014

public class PlayerEntity {
    public int countryId;
    public String playerUrl;
    public String thumbnailUrl;
    public int playerId;
    public String name;

    public PlayerEntity(int countryId, String playerUrl, String thumbnailUrl, int playerId, String name) {
        this.countryId = countryId;
        this.playerUrl = playerUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.playerId = playerId;
        this.name = name;
    }
}
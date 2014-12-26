package com.rajaraman.playerprofile.network.data.entity;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// Created by rajaraman on Dec 25, 2014

public class CountryEntity {
    public String thumbnailUrl;
    public int countryId;
    public String name;

    public CountryEntity(String thumbnailUrl, int countryId, String name) {
        this.thumbnailUrl = thumbnailUrl;
        this.countryId = countryId;
        this.name = name;
    }
}
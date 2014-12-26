package com.rajaraman.playerprofile.network.data.parser;

import android.util.JsonReader;

import com.rajaraman.playerprofile.network.data.entity.CountryEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// Created by rajaraman on Dec 25, 2014

public class JsonDataParser implements DataParser {

    public List parseCountryEntityList(InputStream inputStream) throws IOException {

        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

        if (reader != null) {
            reader.nextName(); // First token will status so skip that
        }

        // Parse the array and return the entity
        return parseCountryEntityList(reader);
    }

    public List parseCountryEntityList(JsonReader reader) throws IOException {

        ArrayList<CountryEntity> countryEntityList = new ArrayList<CountryEntity>();

        reader.beginArray();

        while (reader.hasNext()) {
            countryEntityList.add(parseCountryEntity(reader));
        }

        reader.endArray();

        return countryEntityList;
    }

    public CountryEntity parseCountryEntity(JsonReader reader) throws IOException {

        String thumbnailUrl = null;
        int countryId = 0;
        String name = null;

        reader.beginObject();

        while (reader.hasNext()) {
            String keyName = reader.nextName();

            if (keyName.equals("thumbnailUrl")) {
                thumbnailUrl = reader.nextString();
            } else if (keyName.equals("countryId")) {
                countryId = reader.nextInt();
            } else if (keyName.equals("name")) {
                name = reader.nextString();
            } else {
                reader.skipValue();
            }
        }

        reader.endObject();

        return new CountryEntity(thumbnailUrl, countryId, name);
    }
}
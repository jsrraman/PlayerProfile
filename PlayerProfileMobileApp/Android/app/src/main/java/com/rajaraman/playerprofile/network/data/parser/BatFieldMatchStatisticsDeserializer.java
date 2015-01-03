// Created by rajaraman on Jan 01, 2015
package com.rajaraman.playerprofile.network.data.parser;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.rajaraman.playerprofile.network.data.entities.BatFieldAvg;
import com.rajaraman.playerprofile.network.data.entities.BatFieldMatchStatistics;
import com.rajaraman.playerprofile.network.data.entities.BowlAvg;
import com.rajaraman.playerprofile.network.data.entities.PlayerEntity;

import java.lang.reflect.Type;

public class BatFieldMatchStatisticsDeserializer implements JsonDeserializer<BatFieldMatchStatistics>
{
    @Override
    public BatFieldMatchStatistics deserialize(JsonElement jsonElement, Type type,
                                       JsonDeserializationContext jdc) throws JsonParseException
    {
        // Get the passed Json element as Json object
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        final String matches = jsonObject.get("mat").getAsString();
        final String runs = jsonObject.get("runs").getAsString();
        final String highest = jsonObject.get("highest").getAsString();
        final String average = jsonObject.get("average").getAsString();
        final String hundreds = jsonObject.get("hundreds").getAsString();
        final String fifties = jsonObject.get("fifties").getAsString();

        final BatFieldMatchStatistics batFieldMatchStatistics = new BatFieldMatchStatistics();

        batFieldMatchStatistics.setMatches(matches);
        batFieldMatchStatistics.setRuns(runs);
        batFieldMatchStatistics.setHighest(highest);
        batFieldMatchStatistics.setAverage(average);
        batFieldMatchStatistics.setHundreds(hundreds);
        batFieldMatchStatistics.setFifties(fifties);

        return batFieldMatchStatistics;
    }
}
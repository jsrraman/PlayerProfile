// Created by rajaraman on Jan 01, 2015
package com.rajaraman.playerprofile.network.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class BatFieldAvg {

    private BatFieldMatchStatistics tests;
    private BatFieldMatchStatistics odis;

    public BatFieldMatchStatistics getTests() {
        return tests;
    }

    public void setTests(BatFieldMatchStatistics tests) {
        this.tests = tests;
    }

    public BatFieldMatchStatistics getOdis() {
        return odis;
    }

    public void setOdis(BatFieldMatchStatistics odis) {
        this.odis = odis;
    }
}
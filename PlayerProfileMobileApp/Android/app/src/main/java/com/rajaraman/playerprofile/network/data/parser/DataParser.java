package com.rajaraman.playerprofile.network.data.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

// Created by rajaraman on Dec 25, 2014

public interface DataParser {
    public List parseCountryEntityList(InputStream inputStream) throws IOException;
}

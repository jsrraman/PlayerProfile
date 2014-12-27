package com.rajaraman.playerprofile.network.data.provider;// Created by rajaraman on Dec 27, 2014

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnection {

    public HttpConnection() {}

    public InputStream getData(String url) {
        HttpURLConnection httpCon = null;
        InputStream inputStream = null;

        try {
            httpCon = (HttpURLConnection) (new URL(url)).openConnection();
            httpCon.setRequestMethod("GET");
            httpCon.connect();
            inputStream = httpCon.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return inputStream;
        }
    }
}

// Created by rajaraman on Dec 26, 2014

package com.rajaraman.playerprofile.network.data.provider;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.rajaraman.playerprofile.utils.AppUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlayerProfileApiDataProviderService extends IntentService {
    private static final String TAG = PlayerProfileApiDataProviderService.class.getCanonicalName();

    public PlayerProfileApiDataProviderService() {
        super("PlayerProfileApiDataProviderService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "On Handle Intent - Start");

        ApiReqResData apiReqResData = intent.getParcelableExtra("REQUEST_DATA");
        final ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra("RECEIVER");

        // Request data is a web service url
        String url = apiReqResData.getRequestUrl();

        Bundle b = new Bundle();

        try {
//            HttpURLConnection httpConn = (HttpURLConnection) (new URL(url)).openConnection();
//            httpConn.setRequestMethod("GET");
//            httpConn.connect();
//            InputStream is = httpConn.getInputStream();

            HttpConnection httpConn = new HttpConnection();
            InputStream inputStream = httpConn.getData(url);

            // Convert the input stream into JSON string
            String json = inputStreamToJsonString(inputStream).toString();

            //AppUtil.logDebugMessage(TAG, json);

            apiReqResData.setResponseData(json);
            b.putParcelable("RESPONSE_DATA", apiReqResData);
        } catch (Exception e) {
            e.printStackTrace();
            b.putParcelable("RESPONSE_DATA", null);
        } finally {
            resultReceiver.send(PlayerProfileApiDataProvider.GET_COUNTRY_LIST_CODE, b);
        }
    }

    private StringBuilder inputStreamToJsonString(InputStream is) {
        String rLine = "";
        StringBuilder jsonBuilder = new StringBuilder();

        InputStreamReader isr = new InputStreamReader(is);

        BufferedReader rd = new BufferedReader(isr);

        try {
            while ((rLine = rd.readLine()) != null) {
                jsonBuilder.append(rLine);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return jsonBuilder;
    }
}
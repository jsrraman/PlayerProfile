// Created by rajaraman on Dec 26, 2014

package com.rajaraman.playerprofile.network.data.provider;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ApiDataProviderService extends IntentService {
    private static final String TAG = ApiDataProviderService.class.getCanonicalName();

    public ApiDataProviderService() {
        super("ApiDataProviderService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "On Handle Intent - Start");

        ApiReqResData apiReqResData = intent.getParcelableExtra("REQUEST_DATA");
        final ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra("RECEIVER");

        // Request data is a web service url
        String url = apiReqResData.getRequestUrl();

        HttpConnection httpConn = new HttpConnection();
        Bundle bundle = new Bundle();

        try {
            InputStream inputStream = httpConn.getData(url);

            // Convert the input stream into JSON string
            String json = inputStreamToJsonString(inputStream).toString();

            //AppUtil.logDebugMessage(TAG, json);

            apiReqResData.setResponseData(json);
            bundle.putParcelable("RESPONSE_DATA", apiReqResData);
        } catch (Exception e) {
            e.printStackTrace();
            bundle.putParcelable("RESPONSE_DATA", null);
        } finally {
            httpConn.disconnect();
            resultReceiver.send(apiReqResData.getRequestWebServiceApiId(), bundle);
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
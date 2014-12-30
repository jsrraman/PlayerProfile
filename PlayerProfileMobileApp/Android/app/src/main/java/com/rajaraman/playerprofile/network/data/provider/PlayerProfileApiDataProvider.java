package com.rajaraman.playerprofile.network.data.provider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rajaraman.playerprofile.network.data.entity.CountryEntity;
import com.rajaraman.playerprofile.network.data.entity.PlayerEntity;
import com.rajaraman.playerprofile.utils.AppUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Rajaraman on 25/12/14.
 * This interface provides data from PlayerProfileWebServices
 */
public class PlayerProfileApiDataProvider extends DataProvider implements
                                           ApiDataProviderServiceReceiver.Listener {

    private static final String TAG = PlayerProfileApiDataProvider.class.getCanonicalName();

    public static final int GET_COUNTRY_LIST_API = 0;
    public static final int GET_PLAYER_LIST_FOR_COUNTRY_ID_API = 1;

    public static final String profilePlayerWebServicesBaseUrl = "http://10.0.0.100:3000";
    public static final String countryListUrl = "/players/countries";
    public static final String playerListUrl = "/players/country?countryId=";

    public PlayerProfileApiDataProvider() {}

    // Get the country list
    public void getCountryList(Context context, OnDataReceivedListener onDataReceivedListener) {

        this.context = context;
        this.onDataReceivedListener = onDataReceivedListener;

        String url = this.profilePlayerWebServicesBaseUrl + this.countryListUrl;

        ApiReqResData apiReqResData = new ApiReqResData();
        apiReqResData.setRequestWebServiceApiId(GET_COUNTRY_LIST_API);
        apiReqResData.setRequestUrl(url);

        this.context.startService(createApiDataProviderServiceIntent(apiReqResData));
    }

    // Get the player list for a given country id
    public void getPlayerListForCountry(Context context,
                               OnDataReceivedListener onDataReceivedListener,
                               int countryId) {

        this.context = context;
        this.onDataReceivedListener = onDataReceivedListener;

        String url = this.profilePlayerWebServicesBaseUrl + this.playerListUrl;

        url += Integer.toString(countryId);

        ApiReqResData apiReqResData = new ApiReqResData();
        apiReqResData.setRequestWebServiceApiId(GET_PLAYER_LIST_FOR_COUNTRY_ID_API);
        apiReqResData.setRequestUrl(url);

        this.context.startService(createApiDataProviderServiceIntent(apiReqResData));
    }

    private Intent createApiDataProviderServiceIntent(ApiReqResData apiReqResData) {
        Intent i = new Intent(context, ApiDataProviderService.class);
        ApiDataProviderServiceReceiver receiver =
                                  new ApiDataProviderServiceReceiver(new Handler());
        receiver.setListener(this);

        i.putExtra("REQUEST_DATA", apiReqResData);
        i.putExtra("RECEIVER", receiver);
        return i;
    }

    // Receives the data from ResultReceiver
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        AppUtil.logDebugMessage(TAG, "onReceiveResult");

        Object responseDataObj = resultData.getParcelable("RESPONSE_DATA");

        if (responseDataObj == null) {
            AppUtil.logDebugMessage(TAG, "resultData is null. This is unexpected !!!");
            this.onDataReceivedListener.onDataFetched(null);
            return;
        }

        Object parsedResponseData = null;

        // Get the parcelable data from the resultData
        ApiReqResData apiReqResData = (ApiReqResData) responseDataObj;

        // Get the actual response data from the parcelable data
        String jsonData = apiReqResData.getResponseData();

        switch (resultCode) {
            case GET_COUNTRY_LIST_API: {
                parsedResponseData = getCountryListFromJson(jsonData);
                break;
            }

            case GET_PLAYER_LIST_FOR_COUNTRY_ID_API: {
                parsedResponseData = getPlayerListForCountryFromJson(jsonData);
            }

            default: {
                break;
            }
        }

        this.onDataReceivedListener.onDataFetched(parsedResponseData);
   }

   private ArrayList<CountryEntity> getCountryListFromJson(String jsonData) {

       ArrayList<CountryEntity> countryEntityList = null;

       Gson gson = new Gson();

       try {
           // Get the root JSON object
           JSONObject rootJsonObj = new JSONObject(jsonData);

           // Get the key we are interested in
           JSONArray countryListKeyJsonArray = rootJsonObj.getJSONArray("result");

           // Convert this again to JSON string so that we can use Gson to
           // easily convert (deserialize) this to the actual entity object
           String countryListJsonString = countryListKeyJsonArray.toString();

           //AppUtil.logDebugMessage(TAG, countryListJsonString);

           // convert (Deserialize) JSON string to the equivalent entity object
           countryEntityList = gson.fromJson(countryListJsonString,
                   new TypeToken<ArrayList<CountryEntity>>(){}.getType());
       }catch (Exception e) {
           e.printStackTrace();
       }finally {
          return countryEntityList;
       }
   }

    private ArrayList<PlayerEntity> getPlayerListForCountryFromJson(String jsonData) {

        ArrayList<PlayerEntity> playerEntityList = null;

        Gson gson = new Gson();

        try {
            // Get the root JSON object
            JSONObject rootJsonObj = new JSONObject(jsonData);

            // Get the key we are interested in
            JSONArray playerListKeyJsonArray = rootJsonObj.getJSONArray("result");

            // Convert this again to JSON string so that we can use Gson to
            // easily convert (deserialize) this to the actual entity object
            String playerListJsonString = playerListKeyJsonArray.toString();

            //AppUtil.logDebugMessage(TAG, countryListJsonString);

            // convert (Deserialize) JSON string to the equivalent entity object
            playerEntityList = gson.fromJson(playerListJsonString,
                    new TypeToken<ArrayList<PlayerEntity>>(){}.getType());
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            return playerEntityList;
        }
    }

    // TODO: Revisit this later. Doing this way would be generic.
//    private <T> T getParsedEntityObjectFromJson(String jsonData, final Class<T> genericType) {
//
//        Gson gson = new Gson();
//
//        try {
//            // Get the root JSON object
//            JSONObject rootJsonObj = new JSONObject(jsonData);
//
//            // Get the key we are interested in
//            JSONArray playerListKeyJsonArray = rootJsonObj.getJSONArray("result");
//
//            // Convert this again to JSON string so that we can use Gson to
//            // easily convert (deserialize) this to the actual entity object
//            String playerListJsonString = playerListKeyJsonArray.toString();
//
//            //AppUtil.logDebugMessage(TAG, countryListJsonString);
//
//            // convert (Deserialize) JSON string to the equivalent entity object
//            genericType = gson.fromJson(playerListJsonString,
//                    new TypeToken<genericType>(){}.getType());
//        }catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            return genericType;
//        }
//    }
}
package com.rajaraman.playerprofile.network.data.provider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.gson.Gson;
import com.rajaraman.playerprofile.network.data.entity.CountryEntity;
import com.rajaraman.playerprofile.utils.AppUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Rajaraman on 25/12/14.
 * This interface provides data from PlayerProfileWebServices
 */
public class PlayerProfileApiDataProvider extends DataProvider implements
                                           PlayerProfileApiDataProviderServiceReceiver.Listener {

    private static final String TAG = PlayerProfileApiDataProvider.class.getCanonicalName();

    // Web service names which will be used during request to intentservice
    public static final String GET_COUNTRY_LIST = "GetCountryList";

    // Web service result codes which will be used during response from result receiver
    public static final int GET_COUNTRY_LIST_CODE = 0;

    public static final String profilePlayerWebServicesBaseUrl = "http://10.0.0.100:3000";
    public static final String countryListUrl = "/players/countries";

    public PlayerProfileApiDataProvider() {}

    // Get the country list
    public void getCountryList(Context context,
                               OnDataReceivedListener onDataReceivedListener) {

        this.context = context;
        this.onDataReceivedListener = onDataReceivedListener;

        String url = this.profilePlayerWebServicesBaseUrl + this.countryListUrl;

        ApiReqResData apiReqResData = new ApiReqResData();
        apiReqResData.setRequestWebServiceName(GET_COUNTRY_LIST);
        apiReqResData.setRequestUrl(url);

        this.context.startService(createGetCountryListIntent(apiReqResData));
    }

    private Intent createGetCountryListIntent(ApiReqResData apiReqResData) {
        Intent i = new Intent(context, PlayerProfileApiDataProviderService.class);
        PlayerProfileApiDataProviderServiceReceiver receiver =
                                  new PlayerProfileApiDataProviderServiceReceiver(new Handler());
        receiver.setListener(this);

        i.putExtra("REQUEST_DATA", apiReqResData);
        i.putExtra("RECEIVER", receiver);
        return i;
    }

    // Receives the data from ResultReceiver
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        AppUtil.logDebugMessage(TAG, "onReceiveResult");

        if (resultData == null) {
            AppUtil.logDebugMessage(TAG, "resultData is null. This is unexpected !!!");
            this.onDataReceivedListener.onDataFetched(null);
            return;
        }

        Gson gson = new Gson();

        switch (resultCode) {
            case GET_COUNTRY_LIST_CODE: {

                // Get the parcelable data from the resultData
                ApiReqResData apiReqResData = resultData.getParcelable("RESPONSE_DATA");

                // Get the actual response data from the parcelable data
                String jsonData = apiReqResData.getResponseData();

                try {

                    // Get the root JSON object
                    JSONObject rootJsonObj = new JSONObject(jsonData);

                    // Get the key we are interested in
                    JSONArray countryListKeyJsonArray = rootJsonObj.getJSONArray("countryList");

                    // Convert this again to JSON string so that we can use Gson to
                    // easily convert (deserialize) this to the actual entity object
                    String countryListJsonString = countryListKeyJsonArray.toString();

                    //AppUtil.logDebugMessage(TAG, countryListJsonString);

                    // convert (Deserialize) JSON string to the equivalent entity object
                    CountryEntity[] countryEntityList =
                            gson.fromJson(countryListJsonString, CountryEntity[].class);

                    this.onDataReceivedListener.onDataFetched(countryEntityList);
                }catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            }

            default: {
                break;
            }
        }
    }
}
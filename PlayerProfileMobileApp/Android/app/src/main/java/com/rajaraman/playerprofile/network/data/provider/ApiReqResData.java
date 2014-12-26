// Created by rajaraman on Dec 26, 2014

package com.rajaraman.playerprofile.network.data.provider;

import android.os.Parcel;
import android.os.Parcelable;

public class ApiReqResData implements Parcelable {

    private String requestWebServiceName;
    private String requestUrl;
    private String responseData;

    public ApiReqResData() {}

    public String getRequestWebServiceName() {
        return requestWebServiceName;
    }

    public void setRequestWebServiceName(String requestWebServiceName) {
        this.requestWebServiceName = requestWebServiceName;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // We write the API data information in the parcel
        // Order is important, first request webservice name, requested url and then response data
        dest.writeString(requestWebServiceName);
        dest.writeString(requestUrl);
        dest.writeString(responseData);
    }

    public static final Creator<ApiReqResData> CREATOR = new Creator<ApiReqResData>() {

        @Override
        public ApiReqResData createFromParcel(Parcel source) {
            ApiReqResData apiReqResData = new ApiReqResData();

            apiReqResData.setRequestWebServiceName(source.readString());
            apiReqResData.setRequestUrl(source.readString());
            apiReqResData.setResponseData(source.readString());

            return apiReqResData;
        }

        @Override
        public ApiReqResData[] newArray(int size) {
            return new ApiReqResData[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
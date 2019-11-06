package com.android.arkyumon.data.model.api;

import com.google.gson.annotations.SerializedName;

public class PotholeComplain {

    @SerializedName("complain")
    private String complain;

    @SerializedName("image")
    private String image;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    public String getResponse() {
        return response;
    }

    @SerializedName("response")
    private String response;

}

package com.android.arkyumon.data.remote;

import com.android.arkyumon.data.model.api.PotholeComplain;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("predict")
    Call<PotholeComplain> uploadImage(@Field("message") String complain, @Field("image") String image, @Field("latitude") double latitude,  @Field("longitude") double longitude);
}

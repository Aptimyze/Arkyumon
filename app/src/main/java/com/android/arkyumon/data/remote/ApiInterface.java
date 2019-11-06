package com.android.arkyumon.data.remote;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @Multipart
    @POST("endpoint")
    Call<ResponseBody> uploadImage(
            @Part("description") RequestBody desciption,
            @Part MultipartBody.Part   photo,
            @Part("latitude") float latitude,
            @Part("longitude") float longitude
            );
}

package com.example.mybookkeeper.data.samis;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SbitKenyaLedgerApi {
    @FormUrlEncoded
    @POST(".")
    Call<ResponseBody> phpFunction(@FieldMap Map<String, String> formData);
}

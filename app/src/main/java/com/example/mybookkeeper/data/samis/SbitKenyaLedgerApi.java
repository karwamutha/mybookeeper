package com.example.mybookkeeper.data.samis;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SbitKenyaLedgerApi {

    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

    @FormUrlEncoded
    @POST(".")
    Call<ResponseBody> phpFunction(@FieldMap Map<String, String> formData);

    default <T> List<T> phpFunction(Map<String, String> variables, Type type) throws IOException {
        Call<ResponseBody> function = phpFunction(variables);
        Response<ResponseBody> response = function.execute();
        ResponseBody body = response.body();
        String string;
        if (response.isSuccessful() && body != null && isNonNullNotEmpty(string = body.string())) {
            return gson.fromJson(string, type);
        }
        return Collections.emptyList();
    }

    default <T> T phpFunction(Map<String, String> variables, Class<T> obj) throws IOException {
        Call<ResponseBody> function = phpFunction(variables);
        Response<ResponseBody> response = function.execute();
        ResponseBody body = response.body();
        String string;
        if (response.isSuccessful() && body != null && isNonNullNotEmpty(string = body.string())) {
            return gson.fromJson(string, obj);
        }
        return null;
    }

    default void phpFunction(String itemType, Object entity) throws IOException {
        HashMap<String, String> map = new HashMap<>();
        map.put("item", itemType);

        JsonObject jsonObject = gson.toJsonTree(entity).getAsJsonObject();
        recursivelyPopulate(map, jsonObject);
        Log.i("UPLOAD", "UPLOADING.. " + map);

        phpFunctionNoBody(map);
    }

    default void recursivelyPopulate(Map<String, String> map, JsonObject object) {
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            if (entry.getValue().isJsonObject()) {
                recursivelyPopulate(map, entry.getValue().getAsJsonObject());
            } else {
                map.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
    }

    default void phpFunctionNoBody(Map<String, String> map) throws IOException {
        Call<ResponseBody> function = phpFunction(map);
        Response<ResponseBody> response = function.execute();
        if (!response.isSuccessful()) {
            throw new IOException("uUnexpected http status: " + response.code());
        }
    }

    static boolean isNonNullNotEmpty(String s) {
        return s != null && !s.trim().isEmpty() && !s.trim().equalsIgnoreCase("null");
    }
}

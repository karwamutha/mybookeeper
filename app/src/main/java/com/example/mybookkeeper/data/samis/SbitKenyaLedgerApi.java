package com.example.mybookkeeper.data.samis;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SbitKenyaLedgerApi {

    ObjectMapper mapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
            .build();

    @FormUrlEncoded
    @POST(".")
    Call<ResponseBody> phpFunction(@FieldMap Map<String, String> formData);

    default <T> List<T> phpFunction(Map<String, String> variables, TypeReference<List<T>> type) throws IOException {
        Log.i("REQ_RES", "REQUESTING->List... " + variables);
        Call<ResponseBody> function = phpFunction(variables);
        Response<ResponseBody> response = function.execute();
        ResponseBody body = response.body();
        String string;
        if (response.isSuccessful() && body != null && isNonNullNotEmpty(string = body.string())) {
            Log.i("REQ_RES", "RESPONSE->List... " + string);
            return mapper.readValue(string, type);

        }
        return Collections.emptyList();
    }

    default <T> T phpFunction(Map<String, String> variables, Class<T> obj) throws IOException {
        Log.i("REQ_RES", "REQUESTING->OBJECT... " + variables);
        Call<ResponseBody> function = phpFunction(variables);
        Response<ResponseBody> response = function.execute();
        ResponseBody body = response.body();
        String string;
        if (response.isSuccessful() && body != null && isNonNullNotEmpty(string = body.string())) {
            Log.i("REQ_RES", "RESPONSE... " + string);
            return mapper.readValue(string, obj);
        }
        return null;
    }

    default void phpFunction(String itemType, Object entity) throws IOException {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("item", itemType);

            ObjectNode jsonObject = mapper.valueToTree(entity);
            recursivelyPopulate(map, jsonObject);

            phpFunctionNoBody(map);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    default void recursivelyPopulate(Map<String, String> map, ObjectNode object) {
        Iterator<Map.Entry<String, JsonNode>> fields = object.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            if (entry.getValue().isObject()) {
                recursivelyPopulate(map, (ObjectNode) entry.getValue());
            } else {
                map.put(entry.getKey(), entry.getValue().asText());
            }
        }
    }

    default void phpFunctionNoBody(Map<String, String> map) throws IOException {
        Log.i("REQ_RES", "UPLOADING... " + map);
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

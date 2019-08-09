package com.eks.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;

public class OkHttpUtils {
    private static OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();
    public static JsonElement sendRequest(String urlString) throws IOException {
        Request request = new Request.Builder()
                .url(urlString)
                .get()
                .build();
        Response response = OK_HTTP_CLIENT.newCall(request).execute();
        ResponseBody responseBody = response.body();
        if(responseBody == null){
            throw new RuntimeException("Response is empty.");
        }
        String responseBodyString = responseBody.string();
        return GsonUtils.convertJsonStringToJsonElement(responseBodyString);
    }
    public static JsonObject sendTuLingRobotRequest(JsonObject requestJsonObject) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestJsonObject.toString());
        Request request = new Request.Builder()
                .url("http://openapi.tuling123.com/openapi/api/v2")
                .post(body)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();
        if(responseBody == null){
            throw new RuntimeException("Response is empty.");
        }
        String responseBodyString = responseBody.string();
        return GsonUtils.convertJsonStringToJsonElement(responseBodyString).getAsJsonObject();
    }
}

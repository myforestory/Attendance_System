package com.example.myapplication;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpGetPost {

    OkHttpClient mOkHttpClient;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public void getAsycHttp (String site, JSONObject bean) {
        mOkHttpClient = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url(site);
        requestBuilder.method("GET", null);
        Request request = requestBuilder.build();
        Call mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                    Log.d("Login", "chahe--" + str);
                } else {
                    response.body().string();
                    String str = response.networkResponse().toString();
                    Log.d("Login", "network--" + str);
                }
            }
        });
    }

    OkHttpClient client = new OkHttpClient();

    public String postAsycHttp(String url, String json) {
        String returnMsg = "";
        try {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            returnMsg = response.body().string();
        } catch (IOException e) {
        }
        return returnMsg;
    }

    public static void post(String url, Callback callback, HashMap<String, String> map) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        if(map != null) {
            for (HashMap.Entry<String ,String> entry : map.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

}

package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class initialActivity extends AppCompatActivity {

    private final static String loginURL = "http://kintai-api.ios.tokyo/user/login";
    SharedPreferences loginKey;
    SharedPreferences logoutKey;

    String loginUserName, loginPassword, loginLoginToken;
    String logoutUserName, logoutPassword, logoutLoginToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_initial);

        if(!checkInfo()) {
            Intent intent = new Intent(this,LoginActivity.class);
            Log.d("Aloha", "login");
            finish();
            startActivity(intent);
        } else {
            Intent intent = new Intent(this,MainActivity.class);
            HashMap<String, String> accountMap = new HashMap<String, String>();
            final String userName = getSharedPreferences("loginKey", MODE_PRIVATE).getString("userName", "");
            final String password = getSharedPreferences("loginKey", MODE_PRIVATE).getString("password", "");
            accountMap.put("email", userName);
            accountMap.put("password", password);
            Log.d("Aloha", "main");

            OkHttpGetPost.postAsycHttp(loginURL, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(initialActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("onFailure",  e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody, loginToken, userId, name, email;
                    responseBody = response.body().string();
                    try {

                        JSONObject jsonObject = new JSONObject(responseBody);

                        loginToken = jsonObject.getJSONObject("data").getString("access_token");
                        userId = jsonObject.getJSONObject("data").getJSONObject("logging_in_user").getString("id");
                        name = jsonObject.getJSONObject("data").getJSONObject("logging_in_user").getString("name");
                        email = jsonObject.getJSONObject("data").getJSONObject("logging_in_user").getString("email");
                        Intent intent = new Intent(initialActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("loginToken", loginToken);
                        bundle.putString("userId", userId);
                        bundle.putString("name",name);
                        bundle.putString("email", email);
                        intent.putExtras(bundle);

                        loginKey = getSharedPreferences("loginKey", MODE_PRIVATE);
                        loginKey.edit()
                                .putString("userName", userName)
                                .putString("password", password)
                                .putString("loginToken", loginToken).commit();
                        logoutKey = getSharedPreferences("logoutKey", MODE_PRIVATE);
                        logoutKey.edit()
                                .putString("userName", userName)
                                .putString("password", password)
                                .putString("loginToken", loginToken).commit();

                        startActivity(intent);
                        finish();

                    } catch (JSONException e) {
                        Log.d("JSONException", e.toString());
                    }

                }
            }, accountMap);
        }
    }

    private Boolean checkInfo() {
        Boolean isMatch = false;

        loginUserName = getSharedPreferences("loginKey", MODE_PRIVATE).getString("userName", "0");
        loginPassword = getSharedPreferences("loginKey", MODE_PRIVATE).getString("password", "0");
        loginLoginToken = getSharedPreferences("loginKey", MODE_PRIVATE).getString("loginToken", "0");

        logoutUserName = getSharedPreferences("logoutKey", MODE_PRIVATE).getString("userName", "1");
        logoutPassword = getSharedPreferences("logoutKey", MODE_PRIVATE).getString("password", "1");
        logoutLoginToken = getSharedPreferences("logoutKey", MODE_PRIVATE).getString("loginToken", "1");

        if(loginUserName.equals(logoutUserName) && loginPassword.equals(logoutPassword) && loginLoginToken.equals(logoutLoginToken)) {
            isMatch = true;
        }

        return isMatch;
    }
}

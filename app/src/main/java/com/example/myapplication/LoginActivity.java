package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etUserName, etPassword;
    private Button btnLogin;
    private final static String loginURL = "http://kintai-api.ios.tokyo/user/login";
    SharedPreferences loginKey;
    SharedPreferences logoutKey;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
    }

    private void findViews() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        final OkHttpGetPost okhttpPost = new OkHttpGetPost();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
            }
        });
    }

    private void login() {
        HashMap<String, String> accountMap = new HashMap<String, String>();
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);

        etUserName.setText("chen@marcopolos.co.jp");
        etPassword.setText("test123456");

        final String userName = etUserName.getText().toString();
        final String password = etPassword.getText().toString();

        if (isLegal(userName, password)) {
            accountMap.put("email", userName);
            accountMap.put("password", password);

            OkHttpGetPost.postAsycHttp(loginURL, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("onFailure",  e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody, loginToken, userId, name, email;
                    responseBody = response.body().string();
                    try {

                        JSONObject jsonObject = new JSONObject(responseBody);

                        if (isLegal(jsonObject)){
                            loginToken = jsonObject.getJSONObject("data").getString("access_token");
                            userId = jsonObject.getJSONObject("data").getJSONObject("logging_in_user").getString("id");
                            name = jsonObject.getJSONObject("data").getJSONObject("logging_in_user").getString("name");
                            email = jsonObject.getJSONObject("data").getJSONObject("logging_in_user").getString("email");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
                        }


                    } catch (JSONException e) {
                        Log.d("JSONException", e.toString());
                    }

                }
            }, accountMap);
        }
    }

    private Boolean isLegal (String userName, String password) {
        Boolean isLegal = true;
        if (userName.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "入力してください。", Toast.LENGTH_SHORT).show();
            isLegal = false;
        }
        return isLegal;
    }


    private Boolean isLegal (JSONObject jsonObject) {
        Boolean isLegal = false;
        String returnCode = "";
        try {
            returnCode = jsonObject.getString("code");
            if (!returnCode.isEmpty()) {
                final String errorMsg = jsonObject.getString("message");;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
                isLegal = false;
            } else {
                isLegal = true;
            }
        } catch (JSONException e) {

        }
        return isLegal;
    }
}

package com.example.myapplication;

import android.content.Intent;
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

        etUserName.setText("sou@marcopolos.co.jp");
        etPassword.setText("Aloha1qaz");

        String userName = etUserName.getText().toString();
        String password = etPassword.getText().toString();

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
                    String responseBody, loginToken, userId;
                    responseBody = response.body().string();
                    try {

                        JSONObject jsonObject = new JSONObject(responseBody);

                        if (isLegal(jsonObject)){
                            loginToken = jsonObject.getJSONObject("data").getString("access_token");
                            userId = jsonObject.getJSONObject("data").getJSONObject("logging_in_user").getString("id");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("loginToken", loginToken);
                            bundle.putString("userId", userId);
                            intent.putExtras(bundle);
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
        Boolean isLegal = true;
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

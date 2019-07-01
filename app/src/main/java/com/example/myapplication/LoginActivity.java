package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText etUserName, etPassword;
    private Button btnLogin;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
    }

    private void findViews() {
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etUserName);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();

                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();

                if (isLegal(userName, password)) {
                    bundle.putString("userName", userName);
                    bundle.putString("password", password);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    private Boolean isLegal (String userName, String password) {
        Boolean isLegal = true;
        Log.d("ALoha", String.valueOf(password.isEmpty()));
        if (userName.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "入力してください。", Toast.LENGTH_SHORT).show();
            isLegal = false;
        }

        return isLegal;
    }
}

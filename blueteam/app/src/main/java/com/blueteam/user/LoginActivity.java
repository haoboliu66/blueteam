package com.blueteam.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blueteam.BluetoothChat;
import com.blueteam.R;
import com.blueteam.api.API;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Login activity
 */
public class LoginActivity extends AppCompatActivity {

    EditText etUsername;

    EditText etPassword;

    Button login_btn;

    Button toRegister;

    OkHttpClient okHttpClient = new OkHttpClient();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();

        initEvent();

    }

    public void initView(){
        // components
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        login_btn = findViewById(R.id.log_in);
        toRegister = findViewById(R.id.to_register);
    }

    public void initEvent(){
        // click event
        login_btn.setOnClickListener(new LoginListener());

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "To Register");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * login listener on login button
     */
    private class LoginListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            // validate user input
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("LoginActivity", "Logging in");
            FormBody formBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .build();
                HttpUrl url = new HttpUrl.Builder()
                        .scheme(API.HTTP)
                        .host(API.IP)
                        .port(API.PORT)
                        .addPathSegments(API.LOGIN)
                        .build();
                System.out.println(url);
                login(url, formBody);
        }
    }


    /**
     * login request
     */
    private void login(HttpUrl url, FormBody body) {

        new Thread(() -> {
            Request request = new Request.Builder().url(url).post(body).build();
            System.out.println(request);
            try {
                Response response = okHttpClient.newCall(request).execute();
                System.out.println(response.isSuccessful());
                System.out.println(response.body().string());
                // jump to main UI
                Log.d("LoginActivity","success login");

                Intent intent = new Intent(LoginActivity.this, BluetoothChat.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

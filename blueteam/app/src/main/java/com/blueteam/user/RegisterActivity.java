package com.blueteam.user;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blueteam.R;
import com.blueteam.api.API;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Register activity
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText etrUsername;
    private EditText etrPassword;
    private EditText etConfirmPassword;
    private EditText etEmail;
    private Button submitBtn;
    private Button clearBtn;

    private final String emailCheck = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        initView();

        initEvent();
    }

    private void initView(){
        etrUsername = findViewById(R.id.etr_username);
        etrPassword = findViewById(R.id.etr_password);
        etConfirmPassword = findViewById(R.id.etr_confirm);
        etEmail = findViewById(R.id.etr_email);
        submitBtn = findViewById(R.id.do_register);
        clearBtn = findViewById(R.id.clear);
    }

    private void initEvent(){
        submitBtn.setOnClickListener(new RegisterListener());

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etrUsername.setText("");
                etrPassword.setText("");
                etConfirmPassword.setText("");
                etEmail.setText("");
            }
        });
    }


    private class RegisterListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String username = etrUsername.getText().toString().trim();
            if (username == null || "".equals(username)) {
                Toast.makeText(RegisterActivity.this, "username cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // validate password
            String pwd1 = etrUsername.getText().toString().trim();
            String pwd2 = etConfirmPassword.getText().toString().trim();
            boolean valid = validatePassword(pwd1, pwd2);
            if (!valid) return;
            // validate email
            String email = etEmail.getText().toString().trim();
            Pattern regex = Pattern.compile(emailCheck);
            Matcher matcher = regex.matcher(email);
            boolean flag = matcher.matches();
            if (!flag) {
                Toast.makeText(RegisterActivity.this, "incorrect email format", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("RegisterActivity", "Signing up");
            FormBody formBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password", pwd1)
                    .add("email", email)
                    .build();
            HttpUrl url = new HttpUrl.Builder()
                    .scheme(API.HTTP)
                    .host(API.IP)
                    .port(API.PORT)
                    .addPathSegments(API.REGISTER)
                    .build();
            register(url, formBody);
        }
    }


    /**
     * validate two passwords
     * @param pwd1
     * @param pwd2
     * @return
     */
    public boolean validatePassword(String pwd1, String pwd2) {
        if (pwd1 == null || pwd2 == null || pwd1.length() == 0 || pwd2.length() == 0) {
            Toast.makeText(RegisterActivity.this, "password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!pwd1.equals(pwd2)) {
            Toast.makeText(RegisterActivity.this, "password not consistent", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * post request with form
     * @param url
     * @param form
     */
    public void register(HttpUrl url, FormBody form) {
        new Thread(() -> {
            Request request = new Request.Builder().url(url).post(form).build();
            System.out.println(request);
            try {
                Response response = okHttpClient.newCall(request).execute();
                System.out.println(response.isSuccessful());
                System.out.println(response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


}

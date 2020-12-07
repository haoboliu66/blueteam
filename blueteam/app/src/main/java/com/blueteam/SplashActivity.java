package com.blueteam;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    public static final int DELAY_MILLISECONDS = 3000;

    private Handler mHandler;
    private final Runnable mRunnable = () -> {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, DELAY_MILLISECONDS);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        removeCallbacks();
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeCallbacks();
    }

    private void removeCallbacks() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }
}

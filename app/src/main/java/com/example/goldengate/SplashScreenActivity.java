package com.example.goldengate;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashScreenActivity extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;

    private static final String PREFS_NAME = "com.GoldenGate.GoldenGate.PREFS";
    private static final String TOKEN_KEY = "TOKEN_KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mHandler = new Handler();

        mRunnable = () -> {
            // Check if token exists
            if (isTokenSaved()) {
                // Token exists, redirect to home activity
                startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
            } else {
                // Token does not exist, redirect to login activity
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            }
            finish();
        };

        // Delay for 1000ms (1 second)
        mHandler.postDelayed(mRunnable, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the runnable callback to prevent leaks
        if (mHandler != null && mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    private boolean isTokenSaved() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN_KEY, null);
        return token != null;
    }
    }

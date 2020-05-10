package com.ciber.foodieshoot.applications.detection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ciber.foodieshoot.applications.detection.Authentication.LoginPage;
import com.ciber.foodieshoot.applications.detection.Authentication.SignUp;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showMainActivity();
            }
        }, 1500);
    }

    private void showMainActivity() {
        Intent intent = new Intent(
                SplashActivity.this, LoginPage.class);
        startActivity(intent);
        finish();
    }
}
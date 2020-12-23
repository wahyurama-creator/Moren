package com.wahyurama.moren.App;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.wahyurama.moren.Onboarding.OnboardingScreen;
import com.wahyurama.moren.R;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser() != null) {
                    startActivity(new Intent(SplashActivity.this,
                            MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this,
                            OnboardingScreen.class));
                }
                finish();
            }
        }, 4000);
    }
}
package com.life.medicineandnutrition.smartdlitebr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class SplashActivity extends AppCompatActivity {

    private int TIME_SPLASH = 3000; //7000
    private Boolean isPartner = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (isPartner) {
            WritePartnerActivation(1);
        } else {
            WritePartnerActivation(0);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                SplashActivity.this.finish();
            }
        },TIME_SPLASH);
    }


    private void WritePartnerActivation(Integer value) {

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putInt("ispartner", value);
        myEditor.commit();

    }



}

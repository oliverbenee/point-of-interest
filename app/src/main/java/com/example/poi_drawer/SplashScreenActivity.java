package com.example.poi_drawer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Credit goes to https://abhiandroid.com/programming/splashscreen for how to make the splash screen.
 * The SplashScreen activity is used to show the application name to the user upon startup.
 * It contains a method run(), which starts the MainActivity after 3 seconds.
 * @author AbhiAndroid at https://abhiandroid.com/programming/splashscreen - for guiding the implementation of the splash screen
 * @author Oliver Medoc Benée Petersen, 201806928 - for implementing the splash screen into Point of Interest.
 * @version 3.0
 * @since 05-12-2019
 */
public class SplashScreenActivity extends AppCompatActivity {

    Handler handler;

    /**
     * Create the splash screen and redirect the user to the welcome screen after 3 seconds.
     * source: AbhiAndroid at https://abhiandroid.com/programming/splashscreen - for guiding the implementation of the splash screen
     * @param savedInstanceState the current state of the application. unused.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}

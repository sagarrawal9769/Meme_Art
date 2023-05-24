package com.example.memeart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private static final long SPLASH_SCREEN_DURATION = 2000; // Splash screen duration in milliseconds (3 seconds)

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Set vertical orientation
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.black));
        }

        // Start a timer with the specified duration
        new Handler().postDelayed(() -> {
            // This method will be executed once the timer is finished
            // Start your main activity here
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finish the splash screen activity to prevent going back to it
        }, SPLASH_SCREEN_DURATION);
    }

    // Add any additional logic or methods for your splash screen activity
}

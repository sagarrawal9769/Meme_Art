package com.indiedev91.memeart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "YOUR-TAG-NAME";
    List<String> asciiArts = new ArrayList<>();
    SharedPreferences prefs;
    boolean isFirstLaunch;

    private RecyclerView recyclerView;
    private MemeAdapter adapter;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Set vertical orientation
        try {
            Window window = this.getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(this.getResources().getColor(R.color.black));
            }


            prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            isFirstLaunch = prefs.getBoolean("is_first_launch", true);
            if (isFirstLaunch) {
                // Show alert dialog
                LayoutInflater inflater = LayoutInflater.from(this);
                View infoCardView = inflater.inflate(R.layout.info_cardview_layout, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialogStyle);
                builder.setView(infoCardView)
                        .setInverseBackgroundForced(Boolean.parseBoolean("@color/cardview_dark_background"))
                        .setPositiveButton(R.string.info_card_ok_button, null)
                        .show();
                // Set "is_first_launch" to false
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("is_first_launch", false);
                editor.apply();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        initAsciiArts();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            MemeAdapter adapter = new MemeAdapter(asciiArts, this, recyclerView, this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception here
        }


        MobileAds.initialize(this, initializationStatus -> {
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        try {
            mAdView.loadAd(adRequest);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception here
        }
    }

    public void showInfoCard(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View infoCardView = inflater.inflate(R.layout.info_cardview_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialogStyle);
        builder.setView(infoCardView)
                .setInverseBackgroundForced(Boolean.parseBoolean("@color/cardview_dark_background"))
                .setPositiveButton(R.string.info_card_ok_button, null)
                .show();
    }

    private void initAsciiArts() {
        try {
            asciiArts.addAll(Arrays.asList(
                    getResources().getString(R.string.troll_face1),
                    getResources().getString(R.string.troll_face3),
                    getResources().getString(R.string.troll_face4),
                    getResources().getString(R.string.troll_face_redeyes),
                    getResources().getString(R.string.trollFace6),
                    getResources().getString(R.string.trollFace7),
                    getResources().getString(R.string.nyan_cat),
                    getResources().getString(R.string.mrIncredible),
                    getResources().getString(R.string.mrIncredible2),
                    getResources().getString(R.string.wegogym),
                    getResources().getString(R.string.Awkward_Look_Monkey_Puppet),
                    getResources().getString(R.string.Ghostface),
                    getResources().getString(R.string.ussrFlag),
                    getResources().getString(R.string.apeMonkey),
                    getResources().getString(R.string.cat),
                    getResources().getString(R.string.cat2),
                    getResources().getString(R.string.discordLogo),
                    getResources().getString(R.string.onepunchMan),
                    getResources().getString(R.string.XD),
                    getResources().getString(R.string.putin),
                    getResources().getString(R.string.Jigsaw),
                    getResources().getString(R.string.icanseeyou),
                    getResources().getString(R.string.GG)
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
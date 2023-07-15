package com.indiedev91.memeart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.firebase.database.annotations.Nullable;
import com.indiedev91.memeart.Adapter.FragmentViewPagerAdapter;
import com.indiedev91.memeart.Fragment.ArtFragment;
import com.indiedev91.memeart.Fragment.MemeFragment;
import com.indiedev91.memeart.Fragment.NsfwFragment;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "YOUR-TAG-NAME";
    private static final String SHARED_PREFS_NAME_NSFW = "nsfw_toggle";
    private static final String REWARD_GRANTED_KEY = "nsfw_toggle_key";
    private static final int requestCodeVar = 100;
    SharedPreferences prefs;
    boolean isFirstLaunch;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    FragmentViewPagerAdapter viewPagerAdapter;
    private AppUpdateManager appUpdateManager;

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


            //Init Var views
            tabLayout = findViewById(R.id.tabLayout);
            viewPager = findViewById(R.id.viewPager);


            SharedPreferences mSharedPrefs_nsfw_settings = getSharedPreferences(SHARED_PREFS_NAME_NSFW, Context.MODE_PRIVATE);
            boolean rewardGranted = mSharedPrefs_nsfw_settings.getBoolean(REWARD_GRANTED_KEY, false);

            viewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), rewardGranted);
            viewPager.setAdapter(viewPagerAdapter);
            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                // Set the tab titles using switch-case
                switch (position) {
                    case 0:
                        tab.setText("Meme");
                        break;
                    case 1:
                        tab.setText("Art");
                        break;
                    case 2:
                        if (rewardGranted) {
                            tab.setText("NSFW (18+)");
                        } else {
                            tab.setText("Settings");
                        }
                        break;
                    case 3:
                        tab.setText("Settings");
                        break;
                }
            }).attach();

            appUpdateManager = AppUpdateManagerFactory.create(this);
            appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                        try {
                            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, MainActivity.this,
                                    requestCodeVar);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
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


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == requestCodeVar && resultCode != RESULT_OK) {
            Toast.makeText(this, "Update Cancled", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {

        super.onResume();

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {

                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, MainActivity.this,
                                requestCodeVar);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

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


}
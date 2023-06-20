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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.indiedev91.memeart.Adapter.FragmentViewPagerAdapter;
import com.indiedev91.memeart.Fragment.ArtFragment;
import com.indiedev91.memeart.Fragment.MemeFragment;
import com.indiedev91.memeart.Fragment.NsfwFragment;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "YOUR-TAG-NAME";
    private static final int requestCodeVar = 100;
    private final InstallStateUpdatedListener installStateUpdatedListener = installState -> {
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {

            showSuccsToats();
        }
    };
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
//            adReplacementView();
            //Init Var views
            tabLayout = findViewById(R.id.tabLayout);
            viewPager = findViewById(R.id.viewPager);

            //Setting FragmentManager

            viewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
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
                        tab.setText("NSFW");
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

    private void showSuccsToats() {

        Toast.makeText(this, "Update Installed", Toast.LENGTH_SHORT).show();
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

    public void adReplacementView() {
        //INIT
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        String SHARED_PREFS_NAME_Art, SHARED_PREFS_NAME_Meme, SHARED_PREFS_NAME_Nsfw;
        String REWARD_GRANTED_KEY_Art = null, REWARD_GRANTED_KEY_Meme = null, REWARD_GRANTED_KEY_Nsfw = null;
        SharedPreferences mSharedPrefs_art, mSharedPrefs_meme, mSharedPrefs_nsfw;
        MaterialButton adBtn_art, adBtn_meme, adBtn_nsfw;
        CardView adSpace_art, adSpace_meme, adSpace_nsfw;

        //INITIALIZE
        SHARED_PREFS_NAME_Art = "my_shared_prefs_art";
        SHARED_PREFS_NAME_Meme = "my_shared_prefs_meme";
        SHARED_PREFS_NAME_Nsfw = "my_shared_prefs_nsfw";

        REWARD_GRANTED_KEY_Art = "reward_granted_art";
        REWARD_GRANTED_KEY_Meme = "reward_granted_meme";
        REWARD_GRANTED_KEY_Nsfw = "reward_granted_nsfw";

        FragmentViewPagerAdapter adapter = (FragmentViewPagerAdapter) viewPager.getAdapter();

        mSharedPrefs_art = this.getSharedPreferences(SHARED_PREFS_NAME_Art, Context.MODE_PRIVATE);
        mSharedPrefs_meme = this.getSharedPreferences(SHARED_PREFS_NAME_Meme, Context.MODE_PRIVATE);
        mSharedPrefs_nsfw = this.getSharedPreferences(SHARED_PREFS_NAME_Nsfw, Context.MODE_PRIVATE);

        if (adapter != null) {
            ArtFragment fragment_art = (ArtFragment) adapter.getFragment(1);
            MemeFragment fragment_meme = (MemeFragment) adapter.getFragment(0);
            NsfwFragment fragment_nsfw = (NsfwFragment) adapter.getFragment(2);
            if (fragment_art != null) {
                adBtn_art = fragment_art.getButton_Art();
                adSpace_art = fragment_art.getCardView_Art();

                if (mSharedPrefs_art.getBoolean(REWARD_GRANTED_KEY_Art, false)) {
                    adBtn_art.setVisibility(View.GONE);
                    adSpace_art.setVisibility(View.VISIBLE);
                } else {
                    adBtn_art.setVisibility(View.VISIBLE);
                    adSpace_art.setVisibility(View.GONE);
                }
            }
            if (fragment_meme != null) {
                adBtn_meme = fragment_meme.getButton_Meme();
                adSpace_meme = fragment_meme.getCardView_Meme();

                if (mSharedPrefs_meme.getBoolean(REWARD_GRANTED_KEY_Meme, false)) {
                    adBtn_meme.setVisibility(View.GONE);
                    adSpace_meme.setVisibility(View.VISIBLE);
                } else {
                    adBtn_meme.setVisibility(View.VISIBLE);
                    adSpace_meme.setVisibility(View.GONE);
                }
            }
            if (fragment_nsfw != null) {
                adBtn_nsfw = fragment_nsfw.getButton_Nsfw();
                adSpace_nsfw = fragment_nsfw.getCardView_Nsfw();

                if (mSharedPrefs_nsfw.getBoolean(REWARD_GRANTED_KEY_Nsfw, false)) {
                    adBtn_nsfw.setVisibility(View.GONE);
                    adSpace_nsfw.setVisibility(View.VISIBLE);
                } else {
                    adBtn_nsfw.setVisibility(View.VISIBLE);
                    adSpace_nsfw.setVisibility(View.GONE);
                }
            }

        }


    }

}
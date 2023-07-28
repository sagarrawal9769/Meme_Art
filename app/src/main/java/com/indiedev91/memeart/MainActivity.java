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
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;
import com.google.firebase.database.annotations.Nullable;
import com.indiedev91.memeart.Adapter.FragmentViewPagerAdapter;

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
    private ConsentInformation consentInformation;
    private ConsentForm consentForm;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Init Var views
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

//        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(this)
//                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
//                .addTestDeviceHashedId("3C4F2AB0B239BFFD6D11C116EC7E2CB6")
//                .build();
// Set tag for under age of consent. false means users are not under
        // age.

//        ConsentRequestParameters params = new ConsentRequestParameters
//                .Builder()
//                .setTagForUnderAgeOfConsent(false)
//                .build();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Set vertical orientation
        try {
            Window window = this.getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(this.getResources().getColor(R.color.black));
            }


//            consentInformation = UserMessagingPlatform.getConsentInformation(this);
//            consentInformation.requestConsentInfoUpdate(
//                    this,
//                    params,
//                    new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
//                        @Override
//                        public void onConsentInfoUpdateSuccess() {
//                            // The consent information state was updated.
//                            // You are now ready to check if a form is available.
//                            if (consentInformation.isConsentFormAvailable()) {
//
//                                loadForm();
//                            }
//                        }
//                    },
//                    new ConsentInformation.OnConsentInfoUpdateFailureListener() {
//                        @Override
//                        public void onConsentInfoUpdateFailure(FormError formError) {
//                            // Handle the error.
//                        }
//                    });



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

//    public void loadForm() {
//        // Loads a consent form. Must be called on the main thread.
//        UserMessagingPlatform.loadConsentForm(
//                this,
//                new UserMessagingPlatform.OnConsentFormLoadSuccessListener() {
//                    @Override
//                    public void onConsentFormLoadSuccess(ConsentForm consentForm) {
//                        MainActivity.this.consentForm = consentForm;
//                        if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {
//                            consentForm.show(
//                                    MainActivity.this,
//                                    new ConsentForm.OnConsentFormDismissedListener() {
//                                        @Override
//                                        public void onConsentFormDismissed(@Nullable FormError formError) {
//                                            if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.OBTAINED) {
//                                                // App can start requesting ads.
//
//                                            }
//
//                                            // Handle dismissal by reloading form.
//                                            loadForm();
//                                        }
//                                    });
//                        }
//                    }
//                },
//                new UserMessagingPlatform.OnConsentFormLoadFailureListener() {
//                    @Override
//                    public void onConsentFormLoadFailure(FormError formError) {
//                        // Handle Error.
//                    }
//                }
//        );
//    }

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
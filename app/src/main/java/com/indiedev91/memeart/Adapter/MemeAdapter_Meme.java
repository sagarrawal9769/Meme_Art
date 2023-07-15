package com.indiedev91.memeart.Adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.button.MaterialButton;
import com.indiedev91.memeart.Fragment.MemeFragment;
import com.indiedev91.memeart.MainActivity;
import com.indiedev91.memeart.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MemeAdapter_Meme extends RecyclerView.Adapter<MemeAdapter_Meme.MemeViewHolder> {
    public static final String TAG = "YOUR-TAG-NAME";
    private static final String SHARED_PREFS_NAME = "my_shared_prefs_meme";
    private static final String REWARD_GRANTED_KEY = "reward_granted_meme";
    private final List<String> memeList;
    private final Context context;
    private final MemeFragment binding;
    private final SharedPreferences mSharedPrefs;
    int clickCounter = 0;
    YourClass_meme yourClass_meme_root;
    AdRequest adRequest;

    private RewardedAd rewardedAd;
    private boolean rewardGranted;
    private InterstitialAd mInterstitialAd;


    public MemeAdapter_Meme(List<String> memeList, Context mContext, MemeFragment activity) {
        this.memeList = memeList;
        this.context = mContext;
        mSharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        rewardGranted = mSharedPrefs.getBoolean(REWARD_GRANTED_KEY, false);
        this.binding = activity;
        yourClass_meme_root = new YourClass_meme();  // Move the initialization here
        adRequest = new AdRequest.Builder().build();  // Create AdRequest once
        yourClass_meme_root.intertitalAd(context);
    }


    @NotNull
    @Override
    public MemeViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout, parent, false);
        return new MemeViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NotNull MemeViewHolder holder, int position) {
        try {

            final String meme = memeList.get(position);

            holder.memeTextView_meme.setText(meme);
            if (position == 0) {
                holder.copyImageView_meme.setVisibility(View.VISIBLE);
                holder.lockImageView_meme.setVisibility(View.GONE);
            } else if (mSharedPrefs.getBoolean(REWARD_GRANTED_KEY, false)) {
                holder.copyImageView_meme.setVisibility(View.VISIBLE);
                holder.lockImageView_meme.setVisibility(View.GONE);
            } else {
                holder.copyImageView_meme.setVisibility(View.GONE);
                holder.lockImageView_meme.setVisibility(View.VISIBLE);
            }

            holder.copyImageView_meme.setOnClickListener(v -> {
                try {
                    clickCounter++;
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    String monospaceText = "<font face='monospace'>" + meme + "</font>";
                    ClipData clip = ClipData.newPlainText("meme", meme);
                    if (clickCounter == 2) {
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(binding.requireActivity());
                        }

                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();

                    } else {
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return memeList.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NotNull MemeViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        // Unregister the shared preferences listener
    }

    public class YourClass_meme {
        private boolean isNetworkConnected(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }

        public void intertitalAd(Context context) {
            try {
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(context, "ca-app-pub-3026453700547032/6570403239", adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NotNull InterstitialAd interstitialAd) {
                                mInterstitialAd = interstitialAd;

                                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdClicked() {

                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        mInterstitialAd = null;

                                        intertitalAd(context);
                                        clickCounter = 0;
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NotNull AdError adError) {

                                        mInterstitialAd = null;
                                    }

                                    @Override
                                    public void onAdImpression() {

                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {

                                    }
                                });
                            }

                            @Override
                            public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {

                                mInterstitialAd = null;
                            }
                        });
            } catch (Exception ignored) {

            }
        }
    }

    public class MemeViewHolder extends RecyclerView.ViewHolder {
        TextView memeTextView_meme;
        ImageView copyImageView_meme;
        ImageView lockImageView_meme;
        MaterialButton adBtn_meme;
        CardView adSpace_meme;
        YourClass_meme yourClass_meme = new YourClass_meme();

        // Define the listener for shared preferences changes
        public MemeViewHolder(@NotNull View itemView) {
            super(itemView);
            memeTextView_meme = itemView.findViewById(R.id.trollface);
            copyImageView_meme = itemView.findViewById(R.id.copy);
            lockImageView_meme = itemView.findViewById(R.id.lock);

            View root = binding.requireActivity().getWindow().getDecorView().getRootView();
            adBtn_meme = root.findViewById(R.id.watchAdBtn_meme);
            adSpace_meme = root.findViewById(R.id.adSpace_meme);
            adBtn_meme.setOnClickListener(view -> {
                if (yourClass_meme.isNetworkConnected(context)) {
                    adBtn_meme.setVisibility(View.GONE);
                    Toast.makeText(context, "Loading Ad.....", Toast.LENGTH_LONG).show();
                    // Use the test ad unit ID to load an ad.
                    AdRequest adRequest = new AdRequest.Builder().build();
                    RewardedAd.load(context, "ca-app-pub-3026453700547032/2874700808",
                            adRequest, new RewardedAdLoadCallback() {
                                @Override
                                public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                                    // Handle the error.

                                    rewardedAd = null;
                                    Toast.makeText(context, "Unable to load the ad, please try again is some time ", Toast.LENGTH_SHORT).show();
                                    adBtn_meme.setVisibility(View.VISIBLE);
                                    adSpace_meme.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAdLoaded(@NotNull RewardedAd ad) {
                                    rewardedAd = ad;

                                    Activity activityContext = (Activity) context;
                                    rewardedAd.show(activityContext, rewardItem -> {
                                        // Handle the reward.

                                        rewardGranted = true;
                                        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                            @Override
                                            public void onAdClicked() {
                                                // Called when a click is recorded for an ad.

                                            }

                                            @Override
                                            public void onAdDismissedFullScreenContent() {
                                                // Called when ad is dismissed.
                                                // Set the ad reference to null so you don't show the ad a second time.
                                                rewardedAd = null;
                                                adBtn_meme.setVisibility(View.GONE);
                                                adSpace_meme.setVisibility(View.VISIBLE);
                                                mSharedPrefs.edit().putBoolean(REWARD_GRANTED_KEY, rewardGranted).apply();
                                                Intent intent = new Intent(context, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                context.startActivity(intent);

//                                                for (int i = 0; i < memeList.size(); i++) {
//                                                    // Get the corresponding view holder for this item
//                                                    RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(i);
//
//                                                    // Update the icons for this item
//                                                    if (holder instanceof MemeViewHolder) {
//                                                        MemeViewHolder itemHolder = (MemeViewHolder) holder;
//                                                        itemHolder.copyImageView.setVisibility(View.VISIBLE);
//                                                        itemHolder.lockImageView.setVisibility(View.GONE);
//                                                    }
//                                                }


                                            }

                                            @Override
                                            public void onAdFailedToShowFullScreenContent(@NotNull AdError adError) {
                                                // Called when ad fails to show.

                                                Toast.makeText(context, "Failed to load Ad", Toast.LENGTH_SHORT).show();
                                                rewardedAd = null;
                                                adBtn_meme.setVisibility(View.VISIBLE);
                                                adSpace_meme.setVisibility(View.GONE);

                                            }

                                            @Override
                                            public void onAdImpression() {
                                                // Called when an impression is recorded for an ad.

                                            }

                                            @Override
                                            public void onAdShowedFullScreenContent() {
                                                // Called when ad is shown.

                                            }
                                        });

                                    });
                                }
                            });

                } else {
                    Toast.makeText(context, "No network connection", Toast.LENGTH_SHORT).show();
                    adBtn_meme.setVisibility(View.VISIBLE);
                    adSpace_meme.setVisibility(View.GONE);
                }
            });

        }


    }

}


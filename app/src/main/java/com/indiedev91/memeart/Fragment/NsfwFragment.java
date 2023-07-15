package com.indiedev91.memeart.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.button.MaterialButton;
import com.indiedev91.memeart.Adapter.MemeAdapter_Nsfw;
import com.indiedev91.memeart.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NsfwFragment extends Fragment {
    private static final String SHARED_PREFS_NAME = "my_shared_prefs_nsfw";
    private static final String REWARD_GRANTED_KEY = "reward_granted_nsfw";
    List<String> asciiArts = new ArrayList<>();
    MaterialButton adBtn;
    CardView adSpace;
    SharedPreferences.OnSharedPreferenceChangeListener sharedPrefsListener_nsfw;
    private SharedPreferences mSharedPrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.nsfw_fragment, container, false);


        initAsciiArts_Nsfw();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_nsfw);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adBtn = view.findViewById(R.id.watchAdBtn_nsfw);
        adSpace = view.findViewById(R.id.adSpace_nsfw);
// Initialize SharedPreferences and register the listener
        mSharedPrefs = requireActivity().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        sharedPrefsListener_nsfw = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(REWARD_GRANTED_KEY)) {
                    boolean rewardGranted = sharedPreferences.getBoolean(REWARD_GRANTED_KEY, false);
                    updateAdVisibility(rewardGranted);
                }
            }
        };
        mSharedPrefs.registerOnSharedPreferenceChangeListener(sharedPrefsListener_nsfw);

        // Update the initial visibility based on the value in SharedPreferences
        boolean rewardGranted = mSharedPrefs.getBoolean(REWARD_GRANTED_KEY, false);
        updateAdVisibility(rewardGranted);
        try {
            MemeAdapter_Nsfw adapter = new MemeAdapter_Nsfw(asciiArts, requireContext(), this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception here
        }


        MobileAds.initialize(requireContext(), initializationStatus -> {
        });

        AdView mAdView = view.findViewById(R.id.adView_nsfw);
        AdRequest adRequest = new AdRequest.Builder().build();

        try {
            mAdView.loadAd(adRequest);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception here
        }

        return view;
    }

    private void updateAdVisibility(boolean rewardGranted) {
        if (rewardGranted) {
            adBtn.setVisibility(View.GONE);
            adSpace.setVisibility(View.VISIBLE);
        } else {
            adBtn.setVisibility(View.VISIBLE);
            adSpace.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSharedPrefs.registerOnSharedPreferenceChangeListener(sharedPrefsListener_nsfw);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSharedPrefs.unregisterOnSharedPreferenceChangeListener(sharedPrefsListener_nsfw);
    }

    private void initAsciiArts_Nsfw() {
        try {
            asciiArts.addAll(Arrays.asList(
                    getResources().getString(R.string.anyLoser2),
                    getResources().getString(R.string.ANY_LOSERS),
                    getResources().getString(R.string.among_us2),
                    getResources().getString(R.string.cock)


            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MaterialButton getButton_Nsfw() {
        return adBtn;
    }

    public CardView getCardView_Nsfw() {
        return adSpace;
    }
}
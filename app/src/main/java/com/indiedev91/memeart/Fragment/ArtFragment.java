package com.indiedev91.memeart.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.indiedev91.memeart.Adapter.MemeAdapter_Art;
import com.indiedev91.memeart.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArtFragment extends Fragment {

    private static final String SHARED_PREFS_NAME = "my_shared_prefs_art";
    private static final String REWARD_GRANTED_KEY = "reward_granted_art";
    List<String> asciiArts = new ArrayList<>();
    MaterialButton adBtn;
    CardView adSpace;
    SharedPreferences.OnSharedPreferenceChangeListener sharedPrefsListener;
    private SharedPreferences mSharedPrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.art_frament, container, false);
        initAsciiArts_Art();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_art);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adBtn = view.findViewById(R.id.watchAdBtn_art);
        adSpace = view.findViewById(R.id.adSpace_art);
        // Initialize SharedPreferences and register the listener
        mSharedPrefs = requireActivity().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        sharedPrefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(REWARD_GRANTED_KEY)) {
                    boolean rewardGranted = sharedPreferences.getBoolean(REWARD_GRANTED_KEY, false);
                    updateAdVisibility(rewardGranted);
                }
            }
        };
        mSharedPrefs.registerOnSharedPreferenceChangeListener(sharedPrefsListener);

        // Update the initial visibility based on the value in SharedPreferences
        boolean rewardGranted = mSharedPrefs.getBoolean(REWARD_GRANTED_KEY, false);
        updateAdVisibility(rewardGranted);
        try {
            MemeAdapter_Art adapter = new MemeAdapter_Art(asciiArts, requireContext(), this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception here
        }


        MobileAds.initialize(requireContext(), initializationStatus -> {
        });

        AdView mAdView = view.findViewById(R.id.adView_art);
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
        mSharedPrefs.registerOnSharedPreferenceChangeListener(sharedPrefsListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSharedPrefs.unregisterOnSharedPreferenceChangeListener(sharedPrefsListener);
    }

    private void initAsciiArts_Meme() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("meme");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                for (DataSnapshot artSnapshot : dataSnapshot.getChildren()) {
                    String asciiArt = (String) artSnapshot.getValue();
                    asciiArts.add(asciiArt);
                }

                // Now you can use the asciiArts list as needed
                // ...

                // For example, you can update your existing code:
                // asciiArts.addAll(Arrays.asList(asciiArtsArray));

                // ...

                // Alternatively, if you want to assign the asciiArts list to a member variable
                // of your class, you can do it here.
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                // Handle any errors that may occur
                Log.e("Firebase", "Failed to retrieve art data: " + databaseError.getMessage());
            }
        });
    }

    private void initAsciiArts_Art() {
        try {
            asciiArts.addAll(Arrays.asList(
                    getResources().getString(R.string.mrIncredible2),// IntoArtsAlso
                    getResources().getString(R.string.Ghostface), // IntoArts
                    getResources().getString(R.string.ussrFlag), // IntoArtsAlso
                    getResources().getString(R.string.apeMonkey),// IntoArts
                    getResources().getString(R.string.cat),// IntoArts
                    getResources().getString(R.string.cat2),// IntoArts
                    getResources().getString(R.string.discordLogo),// IntoArts
                    getResources().getString(R.string.onepunchMan),// IntoArts
                    getResources().getString(R.string.XD),// IntoArts
                    getResources().getString(R.string.putin),// IntoArtsAlso
                    getResources().getString(R.string.Jigsaw),// IntoArtsAlso
                    getResources().getString(R.string.icanseeyou),// IntoArtsAlso
                    getResources().getString(R.string.GG),// IntoArts
                    getResources().getString(R.string.heart_2)// IntoArts
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MaterialButton getButton_Art() {
        return adBtn;
    }

    public CardView getCardView_Art() {
        return adSpace;
    }

}
package com.indiedev91.memeart.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.indiedev91.memeart.Adapter.MemeAdapter_Meme;
import com.indiedev91.memeart.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MemeFragment extends Fragment {
    private static final String SHARED_PREFS_NAME = "my_shared_prefs_meme";
    private static final String REWARD_GRANTED_KEY = "reward_granted_meme";
    List<String> asciiArts = new ArrayList<>();
    MaterialButton adBtn;
    CardView adSpace;
    SharedPreferences.OnSharedPreferenceChangeListener sharedPrefsListener_meme;
    MemeAdapter_Meme adapter;
//    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("art");
    private SharedPreferences mSharedPrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meme_fragment, container, false);
//        initAsciiArts_Meme_Local();
//        databaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NotNull Task<DataSnapshot> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(requireContext(), "Retrived Data ", Toast.LENGTH_SHORT).show();
//                    for (DataSnapshot memeSnapshot : task.getResult().getChildren()){
//
//                       String key = memeSnapshot.getKey();
//                        String value = memeSnapshot.getValue(String.class);
//                        Log.i("Firebase_Info", key + ": '" + value + "'");
//                       asciiArts.add(value);
//                        Toast.makeText(requireContext(), "Retrived Data ", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            }
//        });
        initAsciiArts_Meme_Local();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_meme);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adBtn = view.findViewById(R.id.watchAdBtn_meme);
        adSpace = view.findViewById(R.id.adSpace_meme);
        // Initialize SharedPreferences and register the listener
        mSharedPrefs = requireActivity().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        sharedPrefsListener_meme = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(REWARD_GRANTED_KEY)) {
                    boolean rewardGranted = sharedPreferences.getBoolean(REWARD_GRANTED_KEY, false);
                    updateAdVisibility(rewardGranted);
                }
            }
        };
        mSharedPrefs.registerOnSharedPreferenceChangeListener(sharedPrefsListener_meme);

        // Update the initial visibility based on the value in SharedPreferences
        boolean rewardGranted = mSharedPrefs.getBoolean(REWARD_GRANTED_KEY, false);
        updateAdVisibility(rewardGranted);

        try {
            adapter = new MemeAdapter_Meme(asciiArts, requireContext(), this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception here
        }


        MobileAds.initialize(requireContext(), initializationStatus -> {
        });

        AdView mAdView = view.findViewById(R.id.adView_meme);
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
        mSharedPrefs.registerOnSharedPreferenceChangeListener(sharedPrefsListener_meme);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSharedPrefs.unregisterOnSharedPreferenceChangeListener(sharedPrefsListener_meme);
    }




    private void initAsciiArts_Meme_Local() {
        try {
            asciiArts.addAll(Arrays.asList(
                    getResources().getString(R.string.troll_face1),
                    getResources().getString(R.string.troll_face3),
                    getResources().getString(R.string.troll_face4),
                    getResources().getString(R.string.troll_face_redeyes),
                    getResources().getString(R.string.trollFace6),
                    getResources().getString(R.string.trollFace7),
                    getResources().getString(R.string.rick_roll),
                    getResources().getString(R.string.rick_roll1),
                    getResources().getString(R.string.nyan_cat),
                    getResources().getString(R.string.mrIncredible),
                    getResources().getString(R.string.mrIncredible2),// IntoArtsAlso
                    getResources().getString(R.string.wegogym),
                    getResources().getString(R.string.Awkward_Look_Monkey_Puppet),
                    getResources().getString(R.string.ussrFlag), // IntoArtsAlso
                    getResources().getString(R.string.putin),// IntoArtsAlso
                    getResources().getString(R.string.Jigsaw),// IntoArtsAlso
                    getResources().getString(R.string.icanseeyou),// IntoArtsAlso
                    getResources().getString(R.string.moai)
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
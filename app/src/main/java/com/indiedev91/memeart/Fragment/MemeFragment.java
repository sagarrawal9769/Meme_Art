package com.indiedev91.memeart.Fragment;

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
import com.indiedev91.memeart.Adapter.MemeAdapter_Meme;
import com.indiedev91.memeart.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MemeFragment extends Fragment {
    List<String> asciiArts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meme_fragment, container, false);
        initAsciiArts_Meme();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        try {
            MemeAdapter_Meme adapter = new MemeAdapter_Meme(asciiArts, requireContext(), recyclerView, this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception here
        }


        MobileAds.initialize(requireContext(), initializationStatus -> {
        });

        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        try {
            mAdView.loadAd(adRequest);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception here
        }

        return view;
    }

    private void initAsciiArts_Meme() {
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
//                    getResources().getString(R.string.Ghostface), // IntoArts
                    getResources().getString(R.string.ussrFlag), // IntoArtsAlso
//                    getResources().getString(R.string.apeMonkey),// IntoArts
//                    getResources().getString(R.string.cat),// IntoArts
//                    getResources().getString(R.string.cat2),// IntoArts
//                    getResources().getString(R.string.discordLogo),// IntoArts
//                    getResources().getString(R.string.onepunchMan),// IntoArts
//                    getResources().getString(R.string.XD),// IntoArts
                    getResources().getString(R.string.putin),// IntoArtsAlso
                    getResources().getString(R.string.Jigsaw),// IntoArtsAlso
                    getResources().getString(R.string.icanseeyou),// IntoArtsAlso
//                    getResources().getString(R.string.GG),// IntoArts
//                    getResources().getString(R.string.heart_2),// IntoArts
                    getResources().getString(R.string.moai)
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
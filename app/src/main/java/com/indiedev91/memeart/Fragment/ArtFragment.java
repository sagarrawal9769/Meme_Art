package com.indiedev91.memeart.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.indiedev91.memeart.Adapter.MemeAdapter_Art;
import com.indiedev91.memeart.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArtFragment extends Fragment {

    List<String> asciiArts = new ArrayList<>();

    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.art_frament, container, false);
        initAsciiArts_Art();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        try {
            MemeAdapter_Art adapter = new MemeAdapter_Art(asciiArts, requireContext(), recyclerView, this);
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
}
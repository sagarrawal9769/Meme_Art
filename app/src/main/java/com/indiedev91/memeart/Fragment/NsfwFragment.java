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
import com.indiedev91.memeart.Adapter.MemeAdapter_Nsfw;
import com.indiedev91.memeart.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NsfwFragment extends Fragment {
    List<String> asciiArts = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.nsfw_fragment, container, false);


        initAsciiArts_Nsfw();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        try {
            MemeAdapter_Nsfw adapter = new MemeAdapter_Nsfw(asciiArts, requireContext(), recyclerView, this);
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

    private void initAsciiArts_Nsfw() {
        try {
            asciiArts.addAll(Arrays.asList(
                    getResources().getString(R.string.ANY_LOSERS),
                    getResources().getString(R.string.anyLoser2),
                    getResources().getString(R.string.cock)


            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
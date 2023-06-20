package com.indiedev91.memeart.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.indiedev91.memeart.Fragment.ArtFragment;
import com.indiedev91.memeart.Fragment.MemeFragment;
import com.indiedev91.memeart.Fragment.NsfwFragment;
import com.indiedev91.memeart.Fragment.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentViewPagerAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();

    public FragmentViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        fragmentList.add(new MemeFragment());
        fragmentList.add(new ArtFragment());
        fragmentList.add(new NsfwFragment());
        fragmentList.add(new SettingsFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }


    /**
     * Return the number of views available.
     */
    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
    public Fragment getFragment(int position) {
        // Retrieve the fragment at the specified position
        return fragmentList.get(position);
    }
}

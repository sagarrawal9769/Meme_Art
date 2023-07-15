package com.indiedev91.memeart.Adapter;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.indiedev91.memeart.Fragment.ArtFragment;
import com.indiedev91.memeart.Fragment.MemeFragment;
import com.indiedev91.memeart.Fragment.NsfwFragment;
import com.indiedev91.memeart.Fragment.SettingsFragment;
import com.indiedev91.memeart.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FragmentViewPagerAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    MainActivity mainActivity = new MainActivity();
    Context context = mainActivity;

    public FragmentViewPagerAdapter(@NotNull FragmentManager fragmentManager, @NotNull Lifecycle lifecycle, boolean rewardGranted) {
        super(fragmentManager, lifecycle);

        fragmentList.add(new MemeFragment());
        fragmentList.add(new ArtFragment());
        if (rewardGranted) {
            fragmentList.add(new NsfwFragment());
        }
        fragmentList.add(new SettingsFragment());
    }


    @NotNull
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

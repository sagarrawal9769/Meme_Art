package com.indiedev91.memeart.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.indiedev91.memeart.MainActivity;
import com.indiedev91.memeart.R;

public class SettingsFragment extends Fragment {
    private static final String SHARED_PREFS_NAME = "nsfw_toggle";
    private static final String REWARD_GRANTED_KEY = "nsfw_toggle_key";
    MaterialButton contactBtn;

    Switch nsfwSetting;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    private SharedPreferences mSharedPrefs_nsfw_settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        nsfwSetting = view.findViewById(R.id.switchNsfw);
        mSharedPrefs_nsfw_settings = requireContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        contactBtn = view.findViewById(R.id.contactMeBtn);
        contactBtn = view.findViewById(R.id.contactMeBtn);
        tabLayout = requireActivity().findViewById(R.id.tabLayout);
        viewPager = requireActivity().findViewById(R.id.viewPager);

        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {"indiedev9769@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });

        nsfwSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = mSharedPrefs_nsfw_settings.edit();
                editor.putBoolean(REWARD_GRANTED_KEY, isChecked);
                editor.apply();

            }
        });
        nsfwSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartMainActivity();
            }
        });
        nsfwSetting.setChecked(mSharedPrefs_nsfw_settings.getBoolean(REWARD_GRANTED_KEY, false));



        return view;
    }

    private void restartMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
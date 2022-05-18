package com.example.reading_app.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.reading_app.fragment.FragmentAccount;
import com.example.reading_app.fragment.FragmentContent;
import com.example.reading_app.fragment.FragmentHome;
import com.example.reading_app.fragment.FragmentNotification;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new FragmentHome();
            case 1: return new FragmentContent();
            case 2: return new FragmentNotification();
            case 3: return new FragmentAccount();
            default: return new FragmentHome();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}

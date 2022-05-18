package com.example.reading_app.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.reading_app.fragment.bookshelf.FragmentFavoriteBook;
import com.example.reading_app.fragment.bookshelf.FragmentHistoryBook;

public class BookshelfViewPagerAdapter extends FragmentStatePagerAdapter {
    public BookshelfViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new FragmentHistoryBook();
            case 1: return new FragmentFavoriteBook();
            default: return new FragmentHistoryBook();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Lịch sử";
            case 1: return "Yêu thích";
            default: return "Lịch sử";
        }
    }
}

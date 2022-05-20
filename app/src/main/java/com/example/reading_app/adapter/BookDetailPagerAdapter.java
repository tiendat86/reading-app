package com.example.reading_app.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.reading_app.fragment.bookdetail.FragmentReview;
import com.example.reading_app.fragment.bookdetail.FragmentIntroduce;
import com.example.reading_app.fragment.bookdetail.FragmentListChapter;

public class BookDetailPagerAdapter extends FragmentStatePagerAdapter {
    private Integer idBook;
    public BookDetailPagerAdapter(@NonNull FragmentManager fm, Integer behavior, Integer idBook) {
        super(fm, behavior);
        this.idBook = idBook;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new FragmentIntroduce(idBook);
            case 1: return new FragmentReview(idBook);
            case 2: return new FragmentListChapter(idBook);
            default: return new FragmentIntroduce(idBook);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Giới Thiệu";
            case 1: return "Bình Luận";
            case 2: return "D.S. Chương";
            default: return "Giới Thiệu";
        }
    }
}

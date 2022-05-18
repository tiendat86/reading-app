package com.example.reading_app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.reading_app.R;
import com.example.reading_app.adapter.BookshelfViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class FragmentContent extends Fragment {
    private ImageButton iconFilter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iconFilter = view.findViewById(R.id.iconFilter);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        BookshelfViewPagerAdapter adapter = new BookshelfViewPagerAdapter(getChildFragmentManager(), 2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_library_books_24);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_cloud_download_24);
    }
}

package com.example.reading_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.reading_app.adapter.ViewPagerAdapter;
import com.example.reading_app.dal.SQLiteHelper;
import com.example.reading_app.entity.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private BottomNavigationView navigationView;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        setClicked();
    }

    private void initData() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        List<User> list = sqLiteHelper.getListUser();
        if (list.size() > 0) {
            user = list.get(0);
        } else {
            user = null;
        }
        
        viewPager = findViewById(R.id.viewPager);
        navigationView = findViewById(R.id.navigation);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), 4);
        viewPager.setAdapter(adapter);
    }
    
    private void setClicked() {
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mHome:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.mContent:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.mNoti:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.mAccount:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }
}
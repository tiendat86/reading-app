package com.example.reading_app.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.reading_app.MainActivity;
import com.example.reading_app.R;
import com.example.reading_app.dal.SQLiteHelper;
import com.example.reading_app.entity.User;
import com.example.reading_app.ui.authen.LoginActivity;
import com.squareup.picasso.Picasso;

public class FragmentAccount extends Fragment {
    private ImageView imgView;
    private TextView eName, btnLoginLogout;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgView = view.findViewById(R.id.imgView);
        eName = view.findViewById(R.id.eName);
        btnLoginLogout = view.findViewById(R.id.btnLoginLogout);
        declareData();
    }

    private void declareData() {
        if (MainActivity.user == null) {
            btnLoginLogout.setText("Đăng nhập");
            btnLoginLogout.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            });
        } else {
            if (MainActivity.user.getUrlImg() != null && !MainActivity.user.getUrlImg().isEmpty()) {
                Picasso.get().load(MainActivity.user.getUrlImg()).into(imgView);
            }
            eName.setText(MainActivity.user.getFullname());
            btnLoginLogout.setText("Đăng xuất");
            btnLoginLogout.setOnClickListener(v -> {
                SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
                sqLiteHelper.deleteUser(MainActivity.user.getUsername());
                MainActivity.user = null;
                FragmentManager fragmentManager = getParentFragmentManager();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    fragmentManager.beginTransaction().detach(this).commitNow();
                    fragmentManager.beginTransaction().attach(this).commitNow();
                } else {
                    fragmentManager.beginTransaction().detach(this).attach(this).commit();
                }
            });
        }
    }

    public void checkUser() {
        if (MainActivity.user == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Thông báo!");
            builder.setMessage("Cần đăng nhập mới dùng được chức năng này");
            builder.setIcon(R.drawable.notification_blue);
            builder.setNegativeButton("Đóng", (dialog, which) -> {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        declareData();
    }
}

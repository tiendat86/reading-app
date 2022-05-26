package com.example.reading_app.ui.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.reading_app.R;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.dto.request.SearchFilterDTO;
import com.example.reading_app.dto.response.BookResonseDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFilterActivity extends AppCompatActivity {
    private CheckBox cTienhiep, cHuyenhuyen, cKhoahuyen, cVongdu, cDoThi, cDongnhan, cDaSu, cKiemHiep, cKyAo;
    private RadioGroup groupTinhTrang, groupSapXep, groupDangSapXep;
    private EditText eSearch;
    private TextView btnSearch;
    private ImageButton iconBack;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);
        initData();
        setOnClick();
    }

    private void initData() {
        cTienhiep = findViewById(R.id.cTienhiep);
        cHuyenhuyen = findViewById(R.id.cHuyenhuyen);
        cKhoahuyen = findViewById(R.id.cKhoahuyen);
        cVongdu = findViewById(R.id.cVongdu);
        cDoThi = findViewById(R.id.cDoThi);
        cDongnhan = findViewById(R.id.cDongnhan);
        cDaSu = findViewById(R.id.cDaSu);
        cKiemHiep = findViewById(R.id.cKiemHiep);
        cKyAo = findViewById(R.id.cKyAo);
        groupTinhTrang = findViewById(R.id.groupTinhTrang);
        groupSapXep = findViewById(R.id.groupSapXep);
        groupDangSapXep = findViewById(R.id.groupDangSapXep);
        eSearch = findViewById(R.id.eSearch);
        btnSearch = findViewById(R.id.btnSearch);
        iconBack = findViewById(R.id.iconBack);
    }
    
    private void setOnClick() {
        iconBack.setOnClickListener(v -> finish());
        eSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchData();
                    return true;
                }
                return false;
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData();
            }
        });
    }
    
    public void searchData() {
        String name = eSearch.getText().toString() + "";
        List<String> categories = new ArrayList<>();
        if (cTienhiep.isChecked())
            categories.add(getString(R.string.cTienhiep));
        if (cHuyenhuyen.isChecked())
            categories.add(getString(R.string.cHuyenhuyen));
        if (cKhoahuyen.isChecked())
            categories.add(getString(R.string.cKhoahuyen));
        if (cVongdu.isChecked())
            categories.add(getString(R.string.cVongdu));
        if (cDoThi.isChecked())
            categories.add(getString(R.string.cDoThi));
        if (cDongnhan.isChecked())
            categories.add(getString(R.string.cDongnhan));
        if (cDaSu.isChecked())
            categories.add(getString(R.string.cDaSu));
        if (cKiemHiep.isChecked())
            categories.add(getString(R.string.cKiemHiep));
        if (cKyAo.isChecked())
            categories.add(getString(R.string.cKyAo));
        Boolean complete = null;
        if (groupTinhTrang.getCheckedRadioButtonId() == R.id.radioDangra)
            complete = true;
        if (groupTinhTrang.getCheckedRadioButtonId() == R.id.radioHoanthanh)
            complete = false;
        String sortBy = "";
        if (groupSapXep.getCheckedRadioButtonId() == R.id.radioThoigian)
            sortBy = "time";
        if (groupSapXep.getCheckedRadioButtonId() == R.id.radioDanhgia)
            sortBy = "rating";

        // call api
        ApiService.apiService.filterBookCondition(name, categories, complete, sortBy)
                .enqueue(new Callback<List<BookResonseDTO>>() {
                    @Override
                    public void onResponse(Call<List<BookResonseDTO>> call, Response<List<BookResonseDTO>> response) {
                        if (response.code() == 200) {
                            List<BookResonseDTO> list = response.body();
                            if (groupDangSapXep.getCheckedRadioButtonId() == R.id.radioGiamdan)
                                Collections.reverse(list);
                            Intent intent = new Intent(SearchFilterActivity.this, SearchActivity.class);
                            intent.putExtra("filter", (Serializable) list);
                            intent.putExtra("key", eSearch.getText().toString());
                            startActivity(intent);
                        }
                        System.out.println("66666");
                    }

                    @Override
                    public void onFailure(Call<List<BookResonseDTO>> call, Throwable t) {
                        System.out.println("666666");
                    }
                });
    }
}
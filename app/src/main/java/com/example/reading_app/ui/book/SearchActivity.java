package com.example.reading_app.ui.book;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reading_app.R;
import com.example.reading_app.adapter.RecyclerViewBookAdapter;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.dto.response.BookResonseDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewBookAdapter.BookItemListener {
    private ImageButton iconBack;
    private EditText eSearch;
    private TextView btnSearch;
    private RecyclerView recyclerView;
    private RecyclerViewBookAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initData();
    }
    
    private void initData() {
        iconBack = findViewById(R.id.iconBack);
        eSearch = findViewById(R.id.eSearch);
        btnSearch = findViewById(R.id.btnSearch);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerViewBookAdapter(new ArrayList<>());
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setItemListener(this);
        iconBack.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == iconBack) {
            finish();
        } 
        else if (v == btnSearch) {
            callApi();   
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        BookResonseDTO book = adapter.getItemByPosition(position);
        Intent intent = new Intent(getApplicationContext(), BookDetailActivity.class);
        intent.putExtra("book", book);
        startActivity(intent);
    }

    private void callApi() {
        ApiService.apiService.searchBookByKey(eSearch.getText().toString())
                .enqueue(new Callback<List<BookResonseDTO>>() {
                    @Override
                    public void onResponse(Call<List<BookResonseDTO>> call, Response<List<BookResonseDTO>> response) {
                        if (response.code() == 200) {
                            List<BookResonseDTO> list = response.body();
                            adapter.setList(list);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<BookResonseDTO>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Không lấy được dữ liệu", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
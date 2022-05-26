package com.example.reading_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reading_app.R;
import com.example.reading_app.adapter.RecyclerViewBookImgAdapter;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.dto.response.BookResonseDTO;
import com.example.reading_app.ui.book.BookDetailActivity;
import com.example.reading_app.ui.book.SearchActivity;
import com.example.reading_app.ui.book.SearchFilterActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment implements View.OnClickListener, RecyclerViewBookImgAdapter.BookImgItemListener {
    private ImageButton iconSearch, iconFilter;
    private RecyclerView recyclerViewNew, recyclerViewComplete, recyclerViewNoComplete;
    private RecyclerViewBookImgAdapter adapterNewBook, adapterCompleteBook, adapterNoCompleteBook;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iconSearch = view.findViewById(R.id.iconSearch);
        iconFilter = view.findViewById(R.id.iconFilter);
        iconSearch.setOnClickListener(this);
        iconFilter.setOnClickListener(this);
        // Lấy sách mới cập nhật
        recyclerViewNew = view.findViewById(R.id.recyclerViewNew);
        adapterNewBook = new RecyclerViewBookImgAdapter();
        recyclerViewNew.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNew.setAdapter(adapterNewBook);
        adapterNewBook.setItemListener(this);
        getNewBookApi();
        // Lấy sách mới hoàn thành
        recyclerViewComplete = view.findViewById(R.id.recyclerViewComplete);
        adapterCompleteBook = new RecyclerViewBookImgAdapter();
        recyclerViewComplete.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewComplete.setAdapter(adapterCompleteBook);
        adapterCompleteBook.setItemListener(this);
        getCompleteBookApi();
        // Lấy sách mới chưa hoàn thành
        recyclerViewNoComplete = view.findViewById(R.id.recyclerViewNoComplete);
        adapterNoCompleteBook = new RecyclerViewBookImgAdapter();
        recyclerViewNoComplete.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNoComplete.setAdapter(adapterNoCompleteBook);
        adapterNoCompleteBook.setItemListener(this);
        getNoCompleteBookApi();
    }

    private void getNoCompleteBookApi() {
        ApiService.apiService.getNewBookNotComplete().enqueue(new Callback<List<BookResonseDTO>>() {
            @Override
            public void onResponse(Call<List<BookResonseDTO>> call, Response<List<BookResonseDTO>> response) {
                if (response.code() == 200) {
                    List<BookResonseDTO> list = response.body();
                    adapterNoCompleteBook.setList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<BookResonseDTO>> call, Throwable t) {
            }
        });
    }

    private void getCompleteBookApi() {
        ApiService.apiService.getNewBookComplete().enqueue(new Callback<List<BookResonseDTO>>() {
            @Override
            public void onResponse(Call<List<BookResonseDTO>> call, Response<List<BookResonseDTO>> response) {
                if (response.code() == 200) {
                    adapterCompleteBook.setList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<BookResonseDTO>> call, Throwable t) {
            }
        });
    }

    private void getNewBookApi() {
        ApiService.apiService.getNewBookUpdate().enqueue(new Callback<List<BookResonseDTO>>() {
            @Override
            public void onResponse(Call<List<BookResonseDTO>> call, Response<List<BookResonseDTO>> response) {
                if (response.code() == 200) {
                    List<BookResonseDTO> list = response.body();
                    adapterNewBook.setList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<BookResonseDTO>> call, Throwable t) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == iconSearch) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        } else if (v == iconFilter) {
            Intent intent = new Intent(getActivity(), SearchFilterActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        BookResonseDTO book = null;
        recyclerViewNew.getAdapter();
        if (recyclerViewNew.findContainingItemView(view) != null) {
            book = adapterNewBook.getItem(position);
        } else if (recyclerViewComplete.findContainingItemView(view) != null) {
            book = adapterCompleteBook.getItem(position);
        } else if (recyclerViewNoComplete.findContainingItemView(view) != null) {
            book = adapterNoCompleteBook.getItem(position);
        }
        if (book != null) {
            Intent intent = new Intent(view.getContext(), BookDetailActivity.class);
            intent.putExtra("book", book);
            startActivity(intent);
        }
        
    }
}

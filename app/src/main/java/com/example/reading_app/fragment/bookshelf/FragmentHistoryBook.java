package com.example.reading_app.fragment.bookshelf;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reading_app.MainActivity;
import com.example.reading_app.R;
import com.example.reading_app.adapter.RecyclerViewBookAdapter;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.dto.response.BookResonseDTO;
import com.example.reading_app.entity.User;
import com.example.reading_app.ui.book.BookDetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHistoryBook extends Fragment implements RecyclerViewBookAdapter.BookItemListener {
    private RecyclerView recyclerView;
    private RecyclerViewBookAdapter adapter;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new RecyclerViewBookAdapter(new ArrayList<>());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setItemListener(this);
        // call api
        getBookForUser();
    }

    @Override
    public void onResume() {
        super.onResume();
        getBookForUser();
    }

    private void getBookForUser() {
        User user = MainActivity.user;
        if (user != null) {
            ApiService.apiService.getHistoryBookByUser(user.getUsername()).enqueue(new Callback<List<BookResonseDTO>>() {
                @Override
                public void onResponse(Call<List<BookResonseDTO>> call, Response<List<BookResonseDTO>> response) {
                    if (response.code() == 200) {
                        adapter.setList(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<BookResonseDTO>> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        BookResonseDTO book = adapter.getItemByPosition(position);
        Intent intent = new Intent(getContext(), BookDetailActivity.class);
        intent.putExtra("book", book);
        startActivity(intent);
    }
}

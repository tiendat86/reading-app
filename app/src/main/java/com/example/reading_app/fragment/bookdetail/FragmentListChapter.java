package com.example.reading_app.fragment.bookdetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reading_app.R;
import com.example.reading_app.adapter.RecyclerViewListChapterAdapter;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.dto.response.ChapterResponseDTO;
import com.example.reading_app.entity.Chapter;
import com.example.reading_app.ui.chapter.ChapterContentActivity;
import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentListChapter extends Fragment implements RecyclerViewListChapterAdapter.ListChapterItemListener {
    private Integer idBook;
    private TextView tNumChapter;
    private ImageView iconReverse;
    private RecyclerView recyclerView;
    private RecyclerViewListChapterAdapter adapter;
    private Integer numChapter;

    public FragmentListChapter(Integer idBook) {
        this.idBook = idBook;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_chapter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tNumChapter = view.findViewById(R.id.tNumChapter);
        iconReverse = view.findViewById(R.id.iconReverse);
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new RecyclerViewListChapterAdapter(new ArrayList<>());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setItemListener(this);
        iconReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ChapterResponseDTO> list = adapter.getList();
                Collections.reverse(list);
                adapter.setList(list);
            }
        });
        callApi();
    }

    private void callApi() {
        ApiService.apiService.getChapterDetails(idBook).enqueue(new Callback<List<ChapterResponseDTO>>() {
            @Override
            public void onResponse(Call<List<ChapterResponseDTO>> call, Response<List<ChapterResponseDTO>> response) {
                if (response.code() == 200) {
                    List<ChapterResponseDTO> list = response.body();
                    numChapter = list.size();
                    tNumChapter.setText("Số chương (" + list.size() + ")");
                    adapter.setList(list);
                }
            }

            @Override
            public void onFailure(Call<List<ChapterResponseDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Can't get detail chapter", Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onClick(View view, int position) {
        ChapterResponseDTO chapter = adapter.getItem(position);
        getDetailChapterApi(chapter.getId());
        
    }

    private void getDetailChapterApi(Integer id) {
        ApiService.apiService.getChapterById(id).enqueue(new Callback<Chapter>() {
            @Override
            public void onResponse(Call<Chapter> call, Response<Chapter> response) {
                if (response.code() == 200) {
                    Intent intent = new Intent(getContext(), ChapterContentActivity.class);
                    intent.putExtra("chapter", response.body());
                    intent.putExtra("numChapter", numChapter);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Chapter> call, Throwable t) {

            }
        });
    }
}

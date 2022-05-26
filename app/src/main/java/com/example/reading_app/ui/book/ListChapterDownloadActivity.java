package com.example.reading_app.ui.book;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reading_app.R;
import com.example.reading_app.adapter.RecyclerViewListChapterAdapter;
import com.example.reading_app.dal.SQLiteHelper;
import com.example.reading_app.dto.response.BookResonseDTO;
import com.example.reading_app.dto.response.ChapterResponseDTO;
import com.example.reading_app.entity.Chapter;
import com.example.reading_app.ui.chapter.ChapterContentActivity;
import com.example.reading_app.ui.chapter.ChapterContentDownloadActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListChapterDownloadActivity extends AppCompatActivity implements RecyclerViewListChapterAdapter.ListChapterItemListener {
    private Integer idBook;
    private TextView tNumChapter, readContinue;
    private ImageView iconReverse, iconBack;
    private RecyclerView recyclerView;
    private Integer numChapter;
    private BookResonseDTO book;
    private RecyclerViewListChapterAdapter adapter;
    private SQLiteHelper sqLiteHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chapter_download);
        initData();
        setData();
    }

    private void initData() {
        Intent intent = getIntent();
        book = (BookResonseDTO) intent.getSerializableExtra("book");
        sqLiteHelper = new SQLiteHelper(getApplicationContext());
        tNumChapter = findViewById(R.id.tNumChapter);
        iconReverse = findViewById(R.id.iconReverse);
        iconBack = findViewById(R.id.iconBack);
        readContinue = findViewById(R.id.readContinue);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerViewListChapterAdapter(new ArrayList<>());
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), 
                LinearLayoutManager.VERTICAL, false);
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
        readContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (book.getIdChapterLastRead() != null) {
                    Chapter chapter = sqLiteHelper.getDetailChapterById(book.getIdChapterLastRead());
                    Intent intent = new Intent(getApplicationContext(), ChapterContentDownloadActivity.class);
                    intent.putExtra("chapter", chapter);
                    intent.putExtra("numChapter", numChapter);
                    startActivity(intent);
                }
            }
        });
    }

    private void setData() {
        List<ChapterResponseDTO> list = sqLiteHelper.getAllChapterDetail(book.getId());
        numChapter = list.size();
        tNumChapter.setText("Số chương (" + list.size() + ")");
        adapter.setList(list);
        iconBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onClick(View view, int position) {
        ChapterResponseDTO dto = adapter.getItem(position);
        Chapter chapter = sqLiteHelper.getDetailChapterById(dto.getId());
        Intent intent = new Intent(this, ChapterContentDownloadActivity.class);
        intent.putExtra("chapter", chapter);
        intent.putExtra("numChapter", numChapter);
        startActivity(intent);
    }
}
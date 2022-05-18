package com.example.reading_app.ui.chapter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reading_app.MainActivity;
import com.example.reading_app.R;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.common.OnSwipeTouchListener;
import com.example.reading_app.entity.Chapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterContentActivity extends AppCompatActivity {
    private ImageButton iconBack;
    private TextView hTitle, tTitle, tContent;
    private Intent intent;
    private Chapter chapter;
    private Integer numChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_content);
        initData();
    }

    private void initData() {
        intent = getIntent();
        chapter = (Chapter) intent.getSerializableExtra("chapter");
        numChapter = intent.getIntExtra("numChapter", -1);
        iconBack = findViewById(R.id.iconBack);
        hTitle = findViewById(R.id.hTitle);
        tTitle = findViewById(R.id.tTitle);
        tContent = findViewById(R.id.tContent);
        
        iconBack.setOnClickListener(v -> {
            finish();
        });
        setDataChapter();
    }

    private void setDataChapter() {
        // Set value of layout
        hTitle.setText("Chương " + chapter.getNumerical() + "  " + chapter.getTitle());
        tTitle.setText("Chương " + chapter.getNumerical() + "  " + chapter.getTitle());
        tContent.setText(chapter.getContent());
        // Swiper
        tContent.setOnTouchListener(new OnSwipeTouchListener(ChapterContentActivity.this){
            @Override
            public void onSwipeRight() {
                if (chapter.getNumerical() > 1) {
                    getLastChapterApi();
                }
            }

            @Override
            public void onSwipeLeft() {
                if (chapter.getNumerical() < numChapter) {
                    getNextChapterApi();
                }
            }
        });
    }

    private void getLastChapterApi() {
        ApiService.apiService.getLastChapter(chapter.getIdBook(), chapter.getId()).enqueue(new Callback<Chapter>() {
            @Override
            public void onResponse(Call<Chapter> call, Response<Chapter> response) {
                if (response.code() == 200) {
                    intent.putExtra("chapter", response.body());
                    recreate();
                }
            }

            @Override
            public void onFailure(Call<Chapter> call, Throwable t) {
            }
        });
    }

    private void getNextChapterApi() {
        ApiService.apiService.getNextChapter(chapter.getIdBook(), chapter.getId()).enqueue(new Callback<Chapter>() {
            @Override
            public void onResponse(Call<Chapter> call, Response<Chapter> response) {
                if (response.code() == 200) {
                    intent.putExtra("chapter", response.body());
                    recreate();
                }
            }

            @Override
            public void onFailure(Call<Chapter> call, Throwable t) {
            }
        });
    }

    @Override
    public boolean isFinishing() {
        saveStatusReadbookApi();
        return super.isFinishing();
    }

    private void saveStatusReadbookApi() {
        if (MainActivity.user != null) {
            ApiService.apiService.updateStatusBookshelf(MainActivity.user.getUsername(), chapter.getIdBook(),
                    chapter.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }
    }
}
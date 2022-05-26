package com.example.reading_app.ui.chapter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.reading_app.R;
import com.example.reading_app.common.OnSwipeTouchListener;
import com.example.reading_app.dal.SQLiteHelper;
import com.example.reading_app.entity.Chapter;

public class ChapterContentDownloadActivity extends AppCompatActivity {
    private ImageButton iconBack;
    private TextView hTitle, tTitle, tContent;
    private Intent intent;
    private Chapter chapter;
    private Integer numChapter;
    private SQLiteHelper sqLiteHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_content_download);
        initData();
    }

    @Override
    public boolean isFinishing() {
        System.out.println("666666666");
        sqLiteHelper.updateChapterReadNow(chapter.getIdBook(), chapter.getId());
        return super.isFinishing();
    }

    @Override
    public boolean isDestroyed() {
        System.out.println("666666666");
        sqLiteHelper.updateChapterReadNow(chapter.getIdBook(), chapter.getId());
        return super.isDestroyed();
    }

    private void initData() {
        intent = getIntent();
        chapter = (Chapter) intent.getSerializableExtra("chapter");
        sqLiteHelper = new SQLiteHelper(getApplicationContext());
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
        tContent.setOnTouchListener(new OnSwipeTouchListener(ChapterContentDownloadActivity.this){
            @Override
            public void onSwipeRight() {
                if (chapter.getNumerical() > 1) {
                    intent.putExtra("chapter", sqLiteHelper.getLastChapter(chapter.getIdBook(), chapter.getId()));
                    recreate();
                }
            }

            @Override
            public void onSwipeLeft() {
                if (chapter.getNumerical() < numChapter) {
                    intent.putExtra("chapter", sqLiteHelper.getNextChapter(chapter.getIdBook(), chapter.getId()));
                    recreate();
                }
            }
        });
    }
}
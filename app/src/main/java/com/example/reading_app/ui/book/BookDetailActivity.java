package com.example.reading_app.ui.book;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.reading_app.MainActivity;
import com.example.reading_app.R;
import com.example.reading_app.adapter.BookDetailPagerAdapter;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.common.MessageDialogCustom;
import com.example.reading_app.dal.SQLiteHelper;
import com.example.reading_app.dto.response.BookResonseDTO;
import com.example.reading_app.dto.response.ChapterDetailResponseDTO;
import com.example.reading_app.entity.Bookshelf;
import com.example.reading_app.entity.Chapter;
import com.example.reading_app.ui.chapter.ChapterContentActivity;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgBlur, imgView;
    private ImageButton iconBack, btnAddBookShelf;
    private TextView tName, tAuthor, tNumChapter, tComplete, textDetail, tCategories;
    private Button readBook;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BookResonseDTO book;
    private Bookshelf bookshelf;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        book = (BookResonseDTO) intent.getSerializableExtra("book");
        imgBlur = findViewById(R.id.imgBlur);
        imgView = findViewById(R.id.imgView);
        iconBack = findViewById(R.id.iconBack);
        btnAddBookShelf = findViewById(R.id.btnAddBookShelf);
        tName = findViewById(R.id.tName);
        tAuthor = findViewById(R.id.tAuthor);
        tNumChapter = findViewById(R.id.tNumChapter);
        tComplete = findViewById(R.id.tComplete);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        textDetail = findViewById(R.id.textDetail);
        readBook = findViewById(R.id.readBook);
        tCategories = findViewById(R.id.tCategories);
        // Set giá trị mặc định
        Glide.with(getApplicationContext())
                .load(book.getUrlImg())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                .into(imgBlur);
        if (!book.getUrlImg().isEmpty() && book.getUrlImg() != null) {
            Picasso.get().load(book.getUrlImg()).into(imgView);
        }
        tName.setText(book.getName());
        tAuthor.setText(book.getPseudonym());
        tNumChapter.setText("Số chương: " + book.getNumChapter());
        tComplete.setText(book.getComplete() ? "Đã hoàn thành" : "Chưa hoàn thành");
        tCategories.setText(StringUtils.join(book.getCategories(), ", "));
        if (MainActivity.user != null)
            checkBookshelfApi();
        
        // viewpager and adapter
        BookDetailPagerAdapter adapter = new BookDetailPagerAdapter(getSupportFragmentManager(), 3, book.getId());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        
        // Set onclick
        iconBack.setOnClickListener(this);
        btnAddBookShelf.setOnClickListener(v -> {
            if (MainActivity.user == null) {
                new MessageDialogCustom(BookDetailActivity.this, "Cảnh báo" ,
                        "Cần đăng nhập trước khi thêm vào tủ truyện", R.drawable.warning_red)
                        .show();
            } else if (bookshelf == null || bookshelf.getFavorite() == false) {
                new AlertDialog.Builder(this).setTitle("Thông báo")
                        .setMessage("Bạn chắc chắn muốn thêm vào tủ truyện")
                        .setIcon(R.drawable.notification_blue).setNegativeButton("Không", null)
                        .setPositiveButton("Có", (dialog, which) -> addToBookshelfApi())
                        .show();
            } else {
                new AlertDialog.Builder(this).setTitle("Thông báo")
                        .setMessage("Bạn chắc chắn muốn xóa truyện khỏi tủ")
                        .setIcon(R.drawable.notification_blue).setNegativeButton("Không", null)
                        .setPositiveButton("Có", (dialog, which) -> removeBookshelfApi())
                        .show();
            }
        });
        readBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.user == null) {
                    ApiService.apiService.getDetailChapter(book.getId()).enqueue(new Callback<ChapterDetailResponseDTO>() {
                        @Override
                        public void onResponse(Call<ChapterDetailResponseDTO> call, Response<ChapterDetailResponseDTO> response) {
                            callChapterDetailActivity(response.body());
                        }

                        @Override
                        public void onFailure(Call<ChapterDetailResponseDTO> call, Throwable t) {

                        }
                    });
                } else {
                    ApiService.apiService.getDetailChapterUser(MainActivity.user.getUsername(), book.getId()).enqueue(new Callback<ChapterDetailResponseDTO>() {
                        @Override
                        public void onResponse(Call<ChapterDetailResponseDTO> call, Response<ChapterDetailResponseDTO> response) {
                            callChapterDetailActivity(response.body());
                        }

                        @Override
                        public void onFailure(Call<ChapterDetailResponseDTO> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    private void callChapterDetailActivity(ChapterDetailResponseDTO body) {
        Intent intent = new Intent(BookDetailActivity.this, ChapterContentActivity.class);
        Chapter chapter = new Chapter(body.getId(), body.getTitle(), body.getContent(), 
                body.getTime(), body.getNumerical(), body.getIdBook());
        intent.putExtra("chapter", chapter);
        intent.putExtra("numChapter", body.getNumChapter());
        startActivity(intent);
    }

    private void addToBookshelfApi() {
        ApiService.apiService.addBookToBookshelf(true, MainActivity.user.getUsername() ,book.getId())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            finish();
                            new MessageDialogCustom(BookDetailActivity.this, "Thông báo" ,
                                    "Thành công", R.drawable.success_blue)
                                    .show();
                            startActivity(getIntent());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                    }
                });
    }

    private void removeBookshelfApi() {
        ApiService.apiService.removeBookshelf(bookshelf.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    finish();
                    startActivity(getIntent());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    private void checkBookshelfApi() {
        ApiService.apiService.getDetailBookshelf(MainActivity.user.getUsername(), book.getId()).enqueue(new Callback<Bookshelf>() {
            @Override
            public void onResponse(Call<Bookshelf> call, Response<Bookshelf> response) {
                if (response.code() == 200) {
                    bookshelf = response.body();
                    if (bookshelf != null && bookshelf.getFavorite()) {
                        btnAddBookShelf.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
                        textDetail.setText("Xóa khỏi \ntủ truyện");
                    }
                }
            }

            @Override
            public void onFailure(Call<Bookshelf> call, Throwable t) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == iconBack) {
            finish();
        }
    }
}
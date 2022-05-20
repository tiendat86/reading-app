package com.example.reading_app.ui.book;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.reading_app.MainActivity;
import com.example.reading_app.R;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.common.MessageDialogCustom;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriterReviewActivity extends AppCompatActivity {
    private RatingBar ratingReview;
    private EditText contentReview;
    private Button btnCreateReview;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        ratingReview = findViewById(R.id.ratingReview);
        contentReview = findViewById(R.id.contentReview);
        btnCreateReview = findViewById(R.id.btnCreateReview);
        
        btnCreateReview.setOnClickListener(v -> apiCreateReview());
    }

    private void apiCreateReview() {
        Intent intent = getIntent();
        Integer idBook = intent.getIntExtra("idBook", -1);
        String rating = ratingReview.getRating() + "";
        String content = contentReview.getText().toString().trim();
        if (content.equals("")) {
            errorMsg("Cần nhập đánh giá");
        } else {
            ApiService.apiService.createReview(content, rating, MainActivity.user.getUsername(), idBook)
                    .enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        new AlertDialog.Builder(WriterReviewActivity.this)
                                .setTitle("Thông báo")
                                .setMessage("Thành công!")
                                .setIcon(R.drawable.success_blue)
                                .setPositiveButton("Đóng", (dialog, which) -> finish()).show();
                    } else {
                        Toast.makeText(WriterReviewActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(WriterReviewActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void errorMsg(String msg) {
        new MessageDialogCustom(this, "Cảnh báo", msg, R.drawable.warning_red).show();
    }
}
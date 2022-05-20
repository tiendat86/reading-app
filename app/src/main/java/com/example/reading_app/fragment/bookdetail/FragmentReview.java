package com.example.reading_app.fragment.bookdetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reading_app.MainActivity;
import com.example.reading_app.R;
import com.example.reading_app.adapter.RecyclerViewReviewAdapter;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.common.MessageDialogCustom;
import com.example.reading_app.dto.response.ReviewResponseDTO;
import com.example.reading_app.entity.Review;
import com.example.reading_app.ui.book.WriterReviewActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentReview extends Fragment {
    private TextView totalComment;
    private FloatingActionButton btnAddComment;
    private Button btnNew, btnLike;
    private RatingBar totalRating;
    private RecyclerView recyclerView;
    private Integer idBook;
    private RecyclerViewReviewAdapter adapter;
    
    public FragmentReview(Integer idBook) {
        this.idBook = idBook;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(view);
        setActionBtn(view);

    }

    private void initData(View view) {
        totalComment = view.findViewById(R.id.totalComment);
        btnAddComment = view.findViewById(R.id.btnAddComment);
        btnNew = view.findViewById(R.id.btnNew);
        btnLike = view.findViewById(R.id.btnLike);
        totalRating = view.findViewById(R.id.totalRating);
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new RecyclerViewReviewAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        callReviewApi();
    }

    private void callReviewApi() {
        ApiService.apiService.getAllReview(idBook).enqueue(new Callback<List<ReviewResponseDTO>>() {
            @Override
            public void onResponse(Call<List<ReviewResponseDTO>> call, Response<List<ReviewResponseDTO>> response) {
                if (response.code() == 200) {
                    List<ReviewResponseDTO> reviews = response.body();
                    Float sum = 0f;
                    for(ReviewResponseDTO review : reviews) {
                        sum += Float.parseFloat(review.getRating());
                    }
                    totalRating.setRating(sum / reviews.size());
                    totalComment.setText("Đánh giá (" + reviews.size() + ")");
                    adapter.setList(reviews);
                }
            }

            @Override
            public void onFailure(Call<List<ReviewResponseDTO>> call, Throwable t) {
                System.out.println("666666");
            }
        });
    }

    private void setActionBtn(View view) {
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.user != null) {
                    Intent intent = new Intent(getContext(), WriterReviewActivity.class);
                    intent.putExtra("idBook", idBook);
                    startActivity(intent);
                } else {
                    errorMsg("Cần đăng nhập để viết đánh giá");
                }
            }
        });
    }

    private void errorMsg(String msg) {
        new MessageDialogCustom(getContext(), "Cảnh báo", msg, R.drawable.warning_red).show();
    }

    private void successMsg(String msg) {
        new MessageDialogCustom(getContext(), "Thông báo", msg, R.drawable.success_blue).show();
    }
}

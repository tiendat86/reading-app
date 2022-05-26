package com.example.reading_app.fragment.bookdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.reading_app.R;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.dto.response.BookDetailResponseDTO;
import com.example.reading_app.dto.response.BookResonseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentIntroduce extends Fragment {
    private Integer idBook;
    private TextView tDetail;
    
    public FragmentIntroduce(Integer idBook) {
        this.idBook = idBook;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_introduce, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tDetail = view.findViewById(R.id.tDetail);
        callApi();
    }

    private void callApi() {
        ApiService.apiService.detailBookById(idBook).enqueue(new Callback<BookDetailResponseDTO>() {
            @Override
            public void onResponse(Call<BookDetailResponseDTO> call, Response<BookDetailResponseDTO> response) {
                if (response.code() == 200) {
                    tDetail.setText(response.body().getDetail());
                }
            }

            @Override
            public void onFailure(Call<BookDetailResponseDTO> call, Throwable t) {
            }
        });
    }
}

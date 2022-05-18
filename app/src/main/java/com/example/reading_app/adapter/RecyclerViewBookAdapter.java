package com.example.reading_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.reading_app.R;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.dto.response.BookResonseDTO;
import com.example.reading_app.entity.Chapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewBookAdapter extends RecyclerView.Adapter<RecyclerViewBookAdapter.BookViewHolder> {
    private BookItemListener itemListener;
    private List<BookResonseDTO> list;

    public RecyclerViewBookAdapter(List<BookResonseDTO> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookResonseDTO item = list.get(position);
        if (!item.getUrlImg().isEmpty() && item.getUrlImg() != null) {
            Picasso.get().load(item.getUrlImg()).into(holder.imgView);
        }
        holder.tName.setText(item.getName());
        holder.tAuthor.setText(item.getPseudonym());
        holder.tNumChapter.setText("Số chương: " + item.getNumChapter());
        holder.tComplete.setText(item.getComplete() ? "Đã hoàn thành" : "Chưa hoàn thành");
        if (item.getIdChapterLastRead() != null) {
            setValueApi(item.getIdChapterLastRead(), holder);
        }
    }

    private void setValueApi(Integer id, BookViewHolder holder) {
        ApiService.apiService.getChapterById(id).enqueue(new Callback<Chapter>() {
            @Override
            public void onResponse(Call<Chapter> call, Response<Chapter> response) {
                if (response.code() == 200) {
                    Chapter chapter = response.body();
                    holder.tChapterNow.setText("Đang đọc: Chương " + chapter.getNumerical() 
                            + "  " + chapter.getTitle());
                }
            }

            @Override
            public void onFailure(Call<Chapter> call, Throwable t) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public BookResonseDTO getItemByPosition(int position) {
        return list.get(position);
    }

    public void setItemListener(BookItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setList(List<BookResonseDTO> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgView;
        private TextView tName, tAuthor, tNumChapter, tComplete, tChapterNow;
        public BookViewHolder(@NonNull View view) {
            super(view);
            imgView = view.findViewById(R.id.imgView);
            tName = view.findViewById(R.id.tName);
            tAuthor = view.findViewById(R.id.tAuthor);
            tNumChapter = view.findViewById(R.id.tNumChapter);
            tComplete = view.findViewById(R.id.tComplete);
            tChapterNow = view.findViewById(R.id.tChapterNow);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.onItemClick(v, getAdapterPosition());
        }
    }
    
    public interface BookItemListener {
        void onItemClick(View view, int position);
    }
}

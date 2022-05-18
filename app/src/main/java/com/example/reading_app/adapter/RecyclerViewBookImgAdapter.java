package com.example.reading_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reading_app.R;
import com.example.reading_app.dto.response.BookResonseDTO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewBookImgAdapter extends RecyclerView.Adapter<RecyclerViewBookImgAdapter.BookImgViewHolder> {
    private BookImgItemListener itemListener;
    private List<BookResonseDTO> list;

    public RecyclerViewBookImgAdapter() {
        list = new ArrayList<>();
    }
    
    public void setList(List<BookResonseDTO> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setItemListener(BookImgItemListener itemListener) {
        this.itemListener = itemListener;
    }
    
    public BookResonseDTO getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public BookImgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_img, parent, false);
        return new BookImgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookImgViewHolder holder, int position) {
        BookResonseDTO item = list.get(position);
        if (!item.getUrlImg().isEmpty() && item.getUrlImg() != null) {
            Picasso.get().load(item.getUrlImg()).into(holder.imgView);
        }
        holder.tName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class BookImgViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgView;
        private TextView tName;
        public BookImgViewHolder(@NonNull View view) {
            super(view);
            imgView = view.findViewById(R.id.imgView);
            tName = view.findViewById(R.id.tName);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.onItemClick(v, getAdapterPosition());
        }
    }
    
    public interface BookImgItemListener {
        void onItemClick(View view, int position);
    }
}

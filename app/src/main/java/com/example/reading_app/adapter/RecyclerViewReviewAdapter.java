package com.example.reading_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reading_app.R;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.dto.response.BookResonseDTO;
import com.example.reading_app.dto.response.ReviewResponseDTO;
import com.example.reading_app.entity.Chapter;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewReviewAdapter extends RecyclerView.Adapter<RecyclerViewReviewAdapter.ReviewViewHolder> {
    private BookItemListener itemListener;
    private List<ReviewResponseDTO> list;

    public RecyclerViewReviewAdapter(List<ReviewResponseDTO> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewResponseDTO item = list.get(position);
        if (!item.getUrlImg().isEmpty() && item.getUrlImg() != null) {
            Picasso.get().load(item.getUrlImg()).into(holder.imgAvatar);
        }
        holder.tName.setText(item.getFullname());
        holder.ratingReview.setRating(Float.parseFloat(item.getRating()));
        holder.tContent.setText(item.getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ReviewResponseDTO getItemByPosition(int position) {
        return list.get(position);
    }

    public void setItemListener(BookItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setList(List<ReviewResponseDTO> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgAvatar;
        private RatingBar ratingReview;
        private TextView tName, tContent;
        public ReviewViewHolder(@NonNull View view) {
            super(view);
            imgAvatar = view.findViewById(R.id.imgAvatar);
            ratingReview = view.findViewById(R.id.ratingReview);
            tContent = view.findViewById(R.id.tContent);
            tName = view.findViewById(R.id.tName);
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

package com.example.reading_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reading_app.R;
import com.example.reading_app.dto.response.ChapterResponseDTO;

import java.util.List;

public class RecyclerViewListChapterAdapter extends RecyclerView.Adapter<RecyclerViewListChapterAdapter.ListChapterViewHolder> {
    private ListChapterItemListener itemListener;
    private List<ChapterResponseDTO> list;

    public RecyclerViewListChapterAdapter(List<ChapterResponseDTO> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ListChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_chapter, parent, false);
        return new ListChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListChapterViewHolder holder, int position) {
        ChapterResponseDTO item = list.get(position);
        holder.eNumChapter.setText(item.getNumerical().toString());
        holder.eTitle.setText("Chương " + item.getNumerical() + "  " + item.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItemListener(ListChapterItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setList(List<ChapterResponseDTO> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    
    public ChapterResponseDTO getItem(int position) {
        return list.get(position);
    }

    public List<ChapterResponseDTO> getList() {
        return list;
    }

    public class ListChapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView eNumChapter, eTitle;
        public ListChapterViewHolder(@NonNull View view) {
            super(view);
            eNumChapter = view.findViewById(R.id.eNumChapter);
            eTitle = view.findViewById(R.id.eTitle);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.onClick(v, getAdapterPosition());
        }
    }
    
    public interface ListChapterItemListener {
        public void onClick(View view, int position);
    }
}

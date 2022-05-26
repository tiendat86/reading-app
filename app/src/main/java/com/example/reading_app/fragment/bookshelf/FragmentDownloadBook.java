package com.example.reading_app.fragment.bookshelf;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reading_app.R;
import com.example.reading_app.adapter.RecyclerViewBookAdapter;
import com.example.reading_app.dal.SQLiteHelper;
import com.example.reading_app.dto.response.BookResonseDTO;
import com.example.reading_app.ui.book.BookDetailActivity;
import com.example.reading_app.ui.book.ListChapterDownloadActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentDownloadBook extends Fragment implements RecyclerViewBookAdapter.BookItemListener {
    private RecyclerView recyclerView;
    private RecyclerViewBookAdapter adapter;
    private SQLiteHelper sqLiteHelper;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_book, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new RecyclerViewBookAdapter(new ArrayList<>());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setItemListener(this);
        sqLiteHelper = new SQLiteHelper(getContext());
        // call api
        getBookDownload();
    }

    @Override
    public void onResume() {
        super.onResume();
        getBookDownload();
    }

    private void getBookDownload() {
        List<BookResonseDTO> books = sqLiteHelper.getAllBook();
        adapter.setList(books);
    }

    @Override
    public void onItemClick(View view, int position) {
        BookResonseDTO book = adapter.getItemByPosition(position);
        Intent intent = new Intent(getContext(), ListChapterDownloadActivity.class);
        intent.putExtra("book", book);
        startActivity(intent);
    }
}

package com.example.reading_app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reading_app.R;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.common.ProgressDialogCustom;
import com.example.reading_app.dal.SQLiteHelper;
import com.example.reading_app.dto.response.BookResonseDTO;
import com.example.reading_app.entity.Chapter;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewBookAdapter extends RecyclerView.Adapter<RecyclerViewBookAdapter.BookViewHolder> {
    private BookItemListener itemListener;
    private List<BookResonseDTO> list;
    private SQLiteHelper sqLiteHelper;
    private Context context;

    public RecyclerViewBookAdapter(List<BookResonseDTO> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        sqLiteHelper = new SQLiteHelper(parent.getContext());
        context = parent.getContext();
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
        holder.tCategories.setText(StringUtils.join(item.getCategories(), ", "));
        holder.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookResonseDTO dto = sqLiteHelper.getBookById(item.getId());
                String[] options;
                if (dto == null)
                    options = new String[]{"Tải truyện"};
                else
                    options = new String[]{"Cập nhật truyện", "Xóa truyện"};
                new AlertDialog.Builder(v.getContext()).setTitle("Lựa chọn chức năng")
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: {
                                        downloadBook(item, dto);
                                        break;
                                    }
                                    case 1: {
                                        deleteBook(item);
                                        break;
                                    }
                                }
                            }
                        }).show();
            }
        });
    }

    private void deleteBook(BookResonseDTO item) {
        new AlertDialog.Builder(context).setTitle("Thông báo")
                .setIcon(R.drawable.warning_red).setMessage("Bạn chắc chắn muốn xóa khỏi tủ truyện")
                .setPositiveButton("Không", null)
                .setNegativeButton("Có", (dialog, which) -> {
                    sqLiteHelper.deleteChapterByIdBook(item.getId());
                    sqLiteHelper.deleteBook(item.getId());
                    new AlertDialog.Builder(context).setTitle("Thông báo")
                            .setIcon(R.drawable.success_blue).setMessage("Xóa truyện thành công")
                            .setPositiveButton("Đóng", null)
                            .show();
                }).show();
    }

    private void downloadBook(BookResonseDTO item, BookResonseDTO dto) {
        ProgressDialogCustom dialogCustom = new ProgressDialogCustom(context, "Đang tải truyện....");
        dialogCustom.show();
        if (dto == null)
            sqLiteHelper.addBook(item);
        saveChapterById(item.getId(), dto);
        dialogCustom.dismiss();
    }

    private void saveChapterById(Integer id, BookResonseDTO dto) {
        ApiService.apiService.getAllChapter(id).enqueue(new Callback<List<Chapter>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {
                List<Chapter> res = response.body();
                if (res != null && res.size() > 0) {
                    res.forEach(chapter -> {
                        if (sqLiteHelper.getDetailChapterById(chapter.getId()) == null)
                            sqLiteHelper.addChapter(chapter);
                    });
                }
                String message;
                if (dto == null) 
                    message = "Tải truyện thành công";
                else
                    message = "Cập nhật truyện thành công";
                new AlertDialog.Builder(context).setTitle("Thông báo")
                        .setIcon(R.drawable.success_blue).setMessage(message)
                        .setPositiveButton("Đóng", null)
                        .show();
            }

            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {
                System.out.println("66666");
                throw new RuntimeException("Something went wrong");
            }
        });
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
        private ImageButton btnSelect;
        private TextView tName, tAuthor, tNumChapter, tComplete, tChapterNow, tCategories;
        public BookViewHolder(@NonNull View view) {
            super(view);
            imgView = view.findViewById(R.id.imgView);
            tName = view.findViewById(R.id.tName);
            tAuthor = view.findViewById(R.id.tAuthor);
            tNumChapter = view.findViewById(R.id.tNumChapter);
            tComplete = view.findViewById(R.id.tComplete);
            tChapterNow = view.findViewById(R.id.tChapterNow);
            tCategories = view.findViewById(R.id.tCategories);
            btnSelect = view.findViewById(R.id.btnSelect);
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

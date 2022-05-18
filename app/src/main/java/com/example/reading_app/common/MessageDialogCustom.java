package com.example.reading_app.common;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.example.reading_app.R;
import com.example.reading_app.ui.book.BookDetailActivity;

public class MessageDialogCustom {
    AlertDialog alertDialog;
    
    public MessageDialogCustom(Context context, String title, String message, Integer idImg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Thông báo")
                .setMessage(message)
                .setIcon(idImg)
                .setPositiveButton("Đóng", null);
        alertDialog = builder.create();
    }
    
    public void show() {
        alertDialog.show();
    }
}

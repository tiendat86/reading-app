package com.example.reading_app.common;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogCustom {
    private ProgressDialog progress;

    public ProgressDialogCustom(Context context, String message) {
        progress = new ProgressDialog(context);
        progress.setMessage(message);
        progress.setCancelable(false);
    }
    
    public void show() {
        progress.show();
    }
    
    public void dismiss() {
        progress.dismiss();
    }
}

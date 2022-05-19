package com.example.reading_app.ui.authen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.reading_app.R;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.common.MessageDialogCustom;
import com.example.reading_app.entity.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText eUsername, eFullname, ePass, ePassRewrite;
    private Button btnRegister;
    private ImageButton btnClose;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        initData();
        setBtnOnClick();
    }

    private void setBtnOnClick() {
        btnClose.setOnClickListener(v -> finish());
        
        btnRegister.setOnClickListener(v -> registerApi());
    }

    private void registerApi() {
        String username = eUsername.getText().toString();
        String password = ePass.getText().toString();
        String passwordRewrite = ePassRewrite.getText().toString();
        String fullname = eFullname.getText().toString().trim();
        if (username.equals("")) {
            errorMsg("Cần nhập tài khoản");
        } else if (username.contains(" ")) {
            errorMsg("Tài khoản không được có dấu cách");  
        } else if (password.equals("")) {
            errorMsg("Cần nhập mật khẩu");
        } else if (!password.equals(passwordRewrite)) {
            errorMsg("Mật khẩu không trùng khớp");
        } else if (fullname.equals("")) {
            errorMsg("Tên không được để trống");
        } else {
            ApiService.apiService.register(username, password, fullname).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        successMsg("Thành công");
                        finish();
                    } else {
                        errorMsg("Tài khoản đã tồn tại");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    errorMsg("Tài khoản đã tồn tại");
                }
            });
        }
    }
    
    private void errorMsg(String msg) {
        new MessageDialogCustom(this, "Cảnh báo", msg, R.drawable.warning_red).show();
    }
    
    private void successMsg(String msg) {
        new MessageDialogCustom(this, "Thông báo", msg, R.drawable.success_blue).show();
    }

    private void initData() {
        eUsername = findViewById(R.id.eUsername);
        eFullname = findViewById(R.id.eFullname);
        ePass = findViewById(R.id.ePass);
        ePassRewrite = findViewById(R.id.ePassRewrite);
        btnRegister = findViewById(R.id.btnRegister);
        btnClose = findViewById(R.id.btnClose);
    }
}
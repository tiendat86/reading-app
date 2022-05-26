package com.example.reading_app.ui.authen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reading_app.MainActivity;
import com.example.reading_app.R;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.common.MessageDialogCustom;
import com.example.reading_app.dal.SQLiteHelper;
import com.example.reading_app.entity.User;
import com.facebook.login.Login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ImageButton loginGoogle, loginFacebook, btnClose;
    private Button btnLogin, btnRegister;
    private EditText eUsername, ePass;
    private final static int REQUEST_CODE_GOOGLE = 10000;
    private final static int REQUEST_CODE_FACEBOOK = 10001;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        loginGoogle = findViewById(R.id.loginGoogle);
        loginFacebook = findViewById(R.id.loginFacebook);
        btnClose = findViewById(R.id.btnClose);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        eUsername = findViewById(R.id.eUsername);
        ePass = findViewById(R.id.ePass);
        
        setOnClickListener();
    }
    
    public void setOnClickListener () {
        btnClose.setOnClickListener(v -> finish());
        
        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, GoogleSignInActivity.class);
                startActivityForResult(intent, REQUEST_CODE_GOOGLE);
            }
        });

        loginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FacebookSignInActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FACEBOOK);
            }
        });

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        
        btnLogin.setOnClickListener(v -> {
            loginProgress();
        });
    }

    private void loginProgress() {
        String username = eUsername.getText().toString();
        String password = ePass.getText().toString();
        if (username.equals(" ") || username.contains(" ")) {
            errorMsg("Sai định dạng tài khoản");
        } else if (password.equals(" ")) {
            errorMsg("Sai định dạng mật khẩu");
        } else {
            ApiService.apiService.login(username, password).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        SQLiteHelper sqLiteHelper = new SQLiteHelper(LoginActivity.this);
                        sqLiteHelper.addUser(response.body());
                        MainActivity.user = response.body();
                        successMsg("Đăng nhập thành công");
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Thông báo")
                                .setMessage("Đăng nhập thành công")
                                .setIcon(R.drawable.success_blue)
                                .setPositiveButton("Đóng", (dialog, which) -> finish()).show();
                        
                    }
                    else {
                        errorMsg("Sai thông tin tài khoản hoặc mật khẩu");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    errorMsg("Sai thông tin tài khoản hoặc mật khẩu");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GOOGLE && resultCode == RESULT_OK) {
            finish();
        } else if (requestCode == REQUEST_CODE_FACEBOOK && resultCode == RESULT_OK) {
            finish();
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    } 
}
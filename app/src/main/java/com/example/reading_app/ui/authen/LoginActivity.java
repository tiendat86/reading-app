package com.example.reading_app.ui.authen;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reading_app.MainActivity;
import com.example.reading_app.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private ImageButton loginGoogle, loginFacebook;
    private final static int REQUEST_CODE_GOOGLE = 10000;
    private final static int REQUEST_CODE_FACEBOOK = 10001;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_login);
        loginGoogle = findViewById(R.id.loginGoogle);
        loginFacebook = findViewById(R.id.loginFacebook);
        
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GOOGLE && resultCode == RESULT_OK) {
            finish();
        } else if (requestCode == REQUEST_CODE_GOOGLE && resultCode != RESULT_OK) {
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
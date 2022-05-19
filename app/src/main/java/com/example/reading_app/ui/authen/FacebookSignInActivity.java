package com.example.reading_app.ui.authen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.reading_app.MainActivity;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.common.ProgressDialogCustom;
import com.example.reading_app.dal.SQLiteHelper;
import com.example.reading_app.entity.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.Login;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacebookSignInActivity extends LoginActivity {
    private static final String EMAIL = "email";
    private CallbackManager callbackManager;
    private ProgressDialogCustom progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Intent intent = new Intent();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        progressDialog = new ProgressDialogCustom(this, "Facebook Sign in...");
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        callbackManager = CallbackManager.Factory.create();
        
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile", "user_friends"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("6666");
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("6666");
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        mUser = mAuth.getCurrentUser();
                        if (authResult.getAdditionalUserInfo().isNewUser()) {
                        }
                        progressDialog.dismiss();
                        success();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(FacebookSignInActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        error();
                        finish();
                    }
                });
    }

    private void success() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        User user = new User(mUser.getUid(), mUser.getDisplayName(), mUser.getPhotoUrl().toString(), null);
        sqLiteHelper.addUser(user);
        MainActivity.user = user;
        ApiService.apiService.createdUser(mUser.getUid(), user.getFullname(), user.getUrlImg(), user.getIdAuthor()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() != 200) {
                    Toast.makeText(FacebookSignInActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(FacebookSignInActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        setResult(RESULT_OK, intent);
        finish();
    }

    private void error() {
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
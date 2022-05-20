
package com.example.reading_app.ui.authen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.reading_app.MainActivity;
import com.example.reading_app.R;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.common.ProgressDialogCustom;
import com.example.reading_app.dal.SQLiteHelper;
import com.example.reading_app.dto.request.UserDetailDTO;
import com.example.reading_app.entity.User;
import com.example.reading_app.fragment.FragmentAccount;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleSignInActivity extends LoginActivity {
    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialogCustom progressDialog;
    private Intent intent = new Intent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialogCustom(this, "Google Sign in...");
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // configre google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("663802598898-a8b4jc7iri5njpk6k2pckm65udh7274c.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.revokeAccess();
        
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage() + "", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
            }
        }
    }

    private void firebaseAuthWithGoogleAccount(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
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
                        Toast.makeText(GoogleSignInActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        error();
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
                    Toast.makeText(GoogleSignInActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(GoogleSignInActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
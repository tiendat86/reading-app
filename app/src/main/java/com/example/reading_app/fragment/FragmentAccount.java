package com.example.reading_app.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.reading_app.MainActivity;
import com.example.reading_app.R;
import com.example.reading_app.api.ApiService;
import com.example.reading_app.common.ConvertBitmapToFile;
import com.example.reading_app.dal.SQLiteHelper;
import com.example.reading_app.entity.User;
import com.example.reading_app.ui.authen.LoginActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAccount extends Fragment {
    private static final int RESULT_LOAD_IMAGE = 14;
    private static final int SELECT_PICTURE = 9;
    private ImageView imgView;
    private TextView eName, btnLoginLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgView = view.findViewById(R.id.imgView);
        eName = view.findViewById(R.id.eName);
        btnLoginLogout = view.findViewById(R.id.btnLoginLogout);
        declareData();
    }

    private void declareData() {
        if (MainActivity.user == null) {
            btnLoginLogout.setText("Đăng nhập");
            btnLoginLogout.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            });
        } else {
            if (MainActivity.user.getUrlImg() != null && !MainActivity.user.getUrlImg().isEmpty()) {
                Picasso.get().load(MainActivity.user.getUrlImg()).into(imgView);
            }
            eName.setText(MainActivity.user.getFullname());
            btnLoginLogout.setText("Đăng xuất");
            btnLoginLogout.setOnClickListener(v -> {
                SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
                sqLiteHelper.deleteUser(MainActivity.user.getUsername());
                MainActivity.user = null;
                FragmentManager fragmentManager = getParentFragmentManager();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    fragmentManager.beginTransaction().detach(this).commitNow();
                    fragmentManager.beginTransaction().attach(this).commitNow();
                } else {
                    fragmentManager.beginTransaction().detach(this).attach(this).commit();
                }
            });
            imgView.setOnClickListener(v -> {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/**");
                startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImg = data.getData();
            if (null != selectedImg) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImg);
                    Bitmap imgBitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, true);
                    imgView.setImageBitmap(imgBitmap);

                    ConvertBitmapToFile convert = new ConvertBitmapToFile(getContext());
                    String nameFile = (MainActivity.user.getUsername());
                    File file = convert.persistImage(bitmap, nameFile);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part fileBody = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                    ApiService.apiService.changeAvatar(MainActivity.user.getUsername(), fileBody).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == 200) {
                                User newUser = response.body();
                                SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
                                sqLiteHelper.editImageUser(newUser.getUrlImg());
                                Picasso.get().load(newUser.getUrlImg()).into(imgView);
                            }
                            System.out.println("66666");
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            System.out.println("77777");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Picasso.get().load(selectedImg).into(imgView);
//                imgView.setImageURI(selectedImg);
//                imgView.setImageResource(R.drawable.img);
            }
        }
    }

    public void checkUser() {
        if (MainActivity.user == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Thông báo!");
            builder.setMessage("Cần đăng nhập mới dùng được chức năng này");
            builder.setIcon(R.drawable.notification_blue);
            builder.setNegativeButton("Đóng", (dialog, which) -> {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        declareData();
    }
}

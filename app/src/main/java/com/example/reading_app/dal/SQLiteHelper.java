package com.example.reading_app.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.reading_app.entity.User;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "readingapp.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE user(" +
                "username TEXT PRIMARY KEY, " +
                "fullname TEXT, urlImg TEXT, " +
                "idAuthor INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
    
    public List<User> getListUser() {
        List<User> res = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor rs = sqLiteDatabase.query("user", null, null,
                null, null, null, null);
        while ((rs != null) && (rs.moveToNext())) {
            res.add(new User(rs.getString(0), rs.getString(1),
                    rs.getString(2), rs.getInt(3)));
        }
        return res;
    }
    
    public long addUser(User user) {
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("fullname", user.getFullname());
        values.put("urlImg", user.getUrlImg());
        values.put("idAuthor", user.getIdAuthor());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert("user", null, values);
    }
    
    public int deleteUser(String username) {
        String whereClause = "username = ?";
        String []whereArgs = {username};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("user", whereClause, whereArgs);
    }
}

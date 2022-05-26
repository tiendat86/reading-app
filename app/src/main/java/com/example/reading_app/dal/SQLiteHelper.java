package com.example.reading_app.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.reading_app.dto.response.BookResonseDTO;
import com.example.reading_app.dto.response.ChapterResponseDTO;
import com.example.reading_app.entity.Chapter;
import com.example.reading_app.entity.User;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
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
        sql = "CREATE TABLE book(" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT, complete TEXT, urlImg TEXT, " +
                "pseudonym TEXT, numChapter INTEGER, " +
                "idChapterLastRead INTEGER, categories TEXT)";
        db.execSQL(sql);
        sql = "CREATE TABLE chapter(" +
                "id INTEGER PRIMARY KEY, " +
                "title TEXT, content TEXT, " +
                "time TEXT, numeriacal INTEGER, " +
                "idBook INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
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

    public long addBook(BookResonseDTO book) {
        ContentValues values = new ContentValues();
        values.put("id", book.getId());
        values.put("name", book.getName());
        values.put("complete", book.getComplete());
        values.put("urlImg", book.getUrlImg());
        values.put("pseudonym", book.getPseudonym());
        values.put("numChapter", book.getNumChapter());
        values.put("idChapterLastRead", book.getIdChapterLastRead());
        values.put("categories", StringUtils.join(book.getCategories(), "|"));
        
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert("book", null, values);
    }

    public long addChapter(Chapter chapter) {
        ContentValues values = new ContentValues();
        values.put("id", chapter.getId());
        values.put("title", chapter.getTitle());
        values.put("content", chapter.getContent());
        values.put("time", chapter.getTime());
        values.put("numeriacal", chapter.getNumerical());
        values.put("idBook", chapter.getIdBook());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert("chapter", null, values);
    }
    
    public void deleteBook(Integer idBook) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = "id = ?";
        String []whereArgs = {idBook + ""};
        sqLiteDatabase.delete("book", whereClause, whereArgs);
    }
    
    public void deleteChapterByIdBook(Integer idBook) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "idBook = ?";
        String []whereArgs = {idBook + ""};
        db.delete("chapter", whereClause, whereArgs);
    }
    
    public List<BookResonseDTO> getAllBook() {
        List<BookResonseDTO> books = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor rs = db.query("book", null, null, null, null, null, null);
        while ((rs != null) && (rs.moveToNext())) {
            books.add(new BookResonseDTO(rs.getInt(0), rs.getString(1), 
                    (rs.getString(2) == "true") ? true : false, rs.getString(3),
                    rs.getString(4), rs.getInt(5), rs.getInt(6),
                    Arrays.asList(rs.getString(7).split("\\|"))));
        }
        return books;
    }

    public BookResonseDTO getBookById(Integer id) {
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = "id = ?";
        String []whereArgs = {id + ""};
        Cursor rs = db.query("book", null, whereClause, whereArgs, null, null, null);
        while ((rs != null) && (rs.moveToNext())) {
            return new BookResonseDTO(rs.getInt(0), rs.getString(1),
                    (rs.getString(2) == "true") ? true : false, rs.getString(3),
                    rs.getString(4), rs.getInt(5), rs.getInt(6),
                    Arrays.asList(rs.getString(7).split("\\|")));
        }
        return null;
    }
    
    public List<ChapterResponseDTO> getAllChapterDetail(Integer idBook) {
        List<ChapterResponseDTO> res = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String []column = {"id", "numeriacal", "title"};
        String whereClause = "idBook = ?";
        String []whereArgs = {idBook + ""};
        Cursor rs = db.query("chapter" , column, whereClause, whereArgs, null, null, null);
        while (rs != null & rs.moveToNext()) {
            res.add(new ChapterResponseDTO(rs.getInt(0), rs.getInt(1), 
                    rs.getString(2), null));
        }
        return res;
    }
    
    public Chapter getDetailChapterById(Integer id) {
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = "id = ?";
        String []whereArgs = {id + ""};
        Cursor rs = db.query("chapter", null, whereClause, whereArgs,
                null, null, null);
        while (rs!= null && rs.moveToNext()) {
            return new Chapter(rs.getInt(0), rs.getString(1), rs.getString(2),
                    rs.getString(3), rs.getInt(4), rs.getInt(5));
        }
        return null;
    }
    
    public Chapter getNextChapter(Integer idBook, Integer idChapter) {
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = "idBook = ? AND id > ?";
        String []whereArgs = {idBook + "", idChapter + ""};
        String orderBy = "id ASC";
        Cursor rs = db.query("chapter", null, whereClause, whereArgs, 
                null, null, orderBy, "1");
        while (rs!= null && rs.moveToNext()) {
            return new Chapter(rs.getInt(0), rs.getString(1), rs.getString(2),
                    rs.getString(3), rs.getInt(4), rs.getInt(5));
        }
        return null;
    }

    public Chapter getLastChapter(Integer idBook, Integer idChapter) {
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = "idBook = ? AND id < ?";
        String []whereArgs = {idBook + "", idChapter + ""};
        String orderBy = "id DESC";
        Cursor rs = db.query("chapter", null, whereClause, whereArgs,
                null, null, orderBy, "1");
        while (rs!= null && rs.moveToNext()) {
            return new Chapter(rs.getInt(0), rs.getString(1), rs.getString(2),
                    rs.getString(3), rs.getInt(4), rs.getInt(5));
        }
        return null;
    }
    
    public void updateChapterReadNow(Integer idBook, Integer idChapter) {
        BookResonseDTO book = getBookById(idBook);
        ContentValues values = new ContentValues();
        values.put("id", book.getId());
        values.put("name", book.getName());
        values.put("complete", book.getComplete());
        values.put("urlImg", book.getUrlImg());
        values.put("pseudonym", book.getPseudonym());
        values.put("numChapter", book.getNumChapter());
        values.put("idChapterLastRead", idChapter);
        values.put("categories", StringUtils.join(book.getCategories(), "|"));
        String whereClause = "id = ?";
        String []whereArgs = {idBook + ""};
        SQLiteDatabase db = getWritableDatabase();
        db.update("book", values, whereClause, whereArgs);
    }
    
    public void editImageUser(String imgUrl) {
        User user = getListUser().get(0);
        user.setUrlImg(imgUrl);
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("fullname", user.getFullname());
        values.put("urlImg", user.getUrlImg());
        values.put("idAuthor", user.getIdAuthor());
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "username = ?";
        String []whereArgs = {user.getUsername()};
        db.update("user", values, whereClause, whereArgs);
    }
    
    public int deleteUser(String username) {
        String whereClause = "username = ?";
        String []whereArgs = {username};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("user", whereClause, whereArgs);
    }
}

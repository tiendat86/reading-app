package com.example.reading_app.api;

import com.example.reading_app.dto.response.BookDetailResponseDTO;
import com.example.reading_app.dto.response.BookResonseDTO;
import com.example.reading_app.dto.response.ChapterDetailResponseDTO;
import com.example.reading_app.dto.response.ChapterResponseDTO;
import com.example.reading_app.dto.response.ReviewResponseDTO;
import com.example.reading_app.entity.Bookshelf;
import com.example.reading_app.entity.Chapter;
import com.example.reading_app.entity.Review;
import com.example.reading_app.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.0.108:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService.class);
    
    @GET("user/find-book/{key}")
    Call<List<BookResonseDTO>> searchBookByKey(@Path("key") String key);

    @GET("user/detail-book/{id}")
    Call<BookDetailResponseDTO> detailBookById(@Path("id") Integer id);

    @GET("user/book/chapter/{id}")
    Call<List<ChapterResponseDTO>> getChapterDetails(@Path("id") Integer id);

    @GET("user/book/detail-chapter/{id}")
    Call<Chapter> getChapterById(@Path("id") Integer id);

    @GET("user/book/chapter/next/{idBook}/{idChapter}")
    Call<Chapter> getNextChapter(@Path("idBook") Integer idBook, @Path("idChapter") Integer idChapter);

    @GET("user/book/chapter/last/{idBook}/{idChapter}")
    Call<Chapter> getLastChapter(@Path("idBook") Integer idBook, @Path("idChapter") Integer idChapter);
    
    @GET("user/book/chapter/{id}")
    Call<ChapterDetailResponseDTO> getDetailChapter(@Path("id") Integer id);

    @POST("chapter/detail")
    @FormUrlEncoded
    Call<ChapterDetailResponseDTO> getDetailChapterUser(@Field("username") String username, @Field("idBook") Integer idBook);
    

    @GET("user/book/last-update")
    Call<List<BookResonseDTO>> getNewBookUpdate();

    @GET("user/book/last-update-complete")
    Call<List<BookResonseDTO>> getNewBookComplete();

    @GET("user/book/last-update-not-complete")
    Call<List<BookResonseDTO>> getNewBookNotComplete();
    
    @POST("create-user")
    @FormUrlEncoded
    Call<Void> createdUser(@Field("username") String username, @Field("fullname") String fullname, 
                           @Field("urlImg") String urlImg, @Field("idAuthor") Integer idAuthor);
    
    @POST("user/bookshelf/add")
    @FormUrlEncoded
    Call<Void> addBookToBookshelf(@Field("favorite") Boolean favorite, 
                                  @Field("idUser") String idUser, 
                                  @Field("idBook") Integer idBook);

    @POST("bookshelf-history")
    @FormUrlEncoded
    Call<List<BookResonseDTO>> getHistoryBookByUser(@Field("username") String username);

    @POST("bookshelf-favorite")
    @FormUrlEncoded
    Call<List<BookResonseDTO>> getFavoriteBookByUser(@Field("username") String username);

    @POST("bookshelf/detail")
    @FormUrlEncoded
    Call<Bookshelf> getDetailBookshelf(@Field("username") String username, @Field("idBook") Integer idBook);

    @GET("bookshelf/remove/{id}")
    Call<Void> removeBookshelf(@Path("id") Integer id);

    @POST("bookshelf/update-status")
    @FormUrlEncoded
    Call<Void> updateStatusBookshelf(@  Field("username") String username, @Field("idBook") Integer idBook,
                                     @Field("idChapter") Integer idChapter);

    @POST("register")
    @FormUrlEncoded
    Call<Void> register(@Field("username") String username, @Field("password") String password,
                                     @Field("fullname") String fullname);

    @POST("login")
    @FormUrlEncoded
    Call<User> login(@Field("username") String username, @Field("password") String password);

    @POST("book/create-review")
    @FormUrlEncoded
    Call<Void> createReview(@Field("content") String content, @Field("rating") String rating,
                             @Field("username") String username, @Field("idBook") Integer idBook);

    @GET("user/book/review/{idBook}")
    Call<List<ReviewResponseDTO>> getAllReview(@Path("idBook") Integer idBook);
}

package com.example.reading_app.entity;

import java.io.Serializable;

public class Chapter implements Serializable {
    private Integer id;
    private String title;
    private String content;
    private String time;
    private Integer numerical;
    private Integer idBook;

    public Chapter() {
    }

    public Chapter(String title, String content, String time, Integer numerical, Integer idBook) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.numerical = numerical;
        this.idBook = idBook;
    }

    public Chapter(Integer id, String title, String content, String time, Integer numerical, Integer idBook) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.numerical = numerical;
        this.idBook = idBook;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setNumerical(Integer numerical) {
        this.numerical = numerical;
    }

    public Integer getIdBook() {
        return idBook;
    }

    public void setIdBook(Integer idBook) {
        this.idBook = idBook;
    }

    public Integer getNumerical() {
        return numerical;
    }
}

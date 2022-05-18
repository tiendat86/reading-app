package com.example.reading_app.dto.response;

import java.io.Serializable;

public class ChapterResponseDTO implements Serializable { 
    private Integer id;
    private Integer numerical;
    private String title;
    private String time;

    public ChapterResponseDTO() {
    }

    public ChapterResponseDTO(Integer numerical, String title, String time) {
        this.numerical = numerical;
        this.title = title;
        this.time = time;
    }

    public ChapterResponseDTO(Integer id, Integer numerical, String title, String time) {
        this.id = id;
        this.numerical = numerical;
        this.title = title;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumerical() {
        return numerical;
    }

    public void setNumerical(Integer numerical) {
        this.numerical = numerical;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

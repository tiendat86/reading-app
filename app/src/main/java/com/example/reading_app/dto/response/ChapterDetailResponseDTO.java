package com.example.reading_app.dto.response;


public class ChapterDetailResponseDTO {
    private Integer id;
    private String title;
    private String content;
    private String time;
    private Integer numerical;
    private Integer idBook;
    private Integer numChapter;

    public ChapterDetailResponseDTO() {
    }

    public ChapterDetailResponseDTO(Integer id, String title, String content, String time, Integer numerical, Integer idBook, Integer numChapter) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.numerical = numerical;
        this.idBook = idBook;
        this.numChapter = numChapter;
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

    public Integer getNumerical() {
        return numerical;
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

    public Integer getNumChapter() {
        return numChapter;
    }

    public void setNumChapter(Integer numChapter) {
        this.numChapter = numChapter;
    }
}

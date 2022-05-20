package com.example.reading_app.dto.response;

public class ReviewResponseDTO {
    private Integer id;
    private String content;
    private String rating;
    private String username;
    private String fullname;
    private String urlImg;
    private Integer idBook;
    protected String createdAt;

    public ReviewResponseDTO() {
    }

    public ReviewResponseDTO(Integer id, String content, String rating, String username, String fullname, String urlImg, Integer idBook, String createdAt) {
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.username = username;
        this.fullname = fullname;
        this.urlImg = urlImg;
        this.idBook = idBook;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public Integer getIdBook() {
        return idBook;
    }

    public void setIdBook(Integer idBook) {
        this.idBook = idBook;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

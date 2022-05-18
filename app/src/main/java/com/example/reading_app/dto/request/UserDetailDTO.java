package com.example.reading_app.dto.request;

import java.io.Serializable;

public class UserDetailDTO implements Serializable {
    private String username;
    private String fullname;
    private String urlImg;
    private Integer idAuthor;

    public UserDetailDTO() {
    }

    public UserDetailDTO(String username, String fullname, String urlImg, Integer idAuthor) {
        this.username = username;
        this.fullname = fullname;
        this.urlImg = urlImg;
        this.idAuthor = idAuthor;
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

    public Integer getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(Integer idAuthor) {
        this.idAuthor = idAuthor;
    }
}

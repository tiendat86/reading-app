package com.example.reading_app.entity;

public class Bookshelf {
    private Integer id;
    private Integer reading;
    private Boolean favorite;
    private String idUser;
    private Integer idBook;

    public Bookshelf() {
    }

    public Bookshelf(Integer id, Integer reading, Boolean favorite, String idUser, Integer idBook) {
        this.id = id;
        this.reading = reading;
        this.favorite = favorite;
        this.idUser = idUser;
        this.idBook = idBook;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReading() {
        return reading;
    }

    public void setReading(Integer reading) {
        this.reading = reading;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Integer getIdBook() {
        return idBook;
    }

    public void setIdBook(Integer idBook) {
        this.idBook = idBook;
    }
}

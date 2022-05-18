package com.example.reading_app.dto.response;

import java.io.Serializable;

public class BookResonseDTO implements Serializable {
    private Integer id;
    private String name;
    private Boolean complete;
    private String urlImg;
    private String pseudonym;
    private Integer numChapter;
    private Integer idChapterLastRead;

    public BookResonseDTO() {
    }

    public BookResonseDTO(Integer id, String name, Boolean complete, String urlImg, String pseudonym, Integer numChapter, Integer idChapterLastRead) {
        this.id = id;
        this.name = name;
        this.complete = complete;
        this.urlImg = urlImg;
        this.pseudonym = pseudonym;
        this.numChapter = numChapter;
        this.idChapterLastRead = idChapterLastRead;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public Integer getNumChapter() {
        return numChapter;
    }

    public void setNumChapter(Integer numChapter) {
        this.numChapter = numChapter;
    }

    public Integer getIdChapterLastRead() {
        return idChapterLastRead;
    }

    public void setIdChapterLastRead(Integer idChapterLastRead) {
        this.idChapterLastRead = idChapterLastRead;
    }
}

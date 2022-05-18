package com.example.reading_app.dto.response;

import java.io.Serializable;

public class BookDetailResponseDTO implements Serializable {
    String detail;

    public BookDetailResponseDTO() {
    }

    public BookDetailResponseDTO(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
package com.example.reading_app.dto.request;

import java.util.List;

public class SearchFilterDTO {
    private String name;
    private List<String> categories;
    private Boolean complete;
    private String sortBy;

    public SearchFilterDTO() {
    }

    public SearchFilterDTO(String name, List<String> categories, Boolean complete, String sortBy) {
        this.name = name;
        this.categories = categories;
        this.complete = complete;
        this.sortBy = sortBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}

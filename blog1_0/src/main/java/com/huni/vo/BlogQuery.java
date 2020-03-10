package com.huni.vo;

public class BlogQuery {
    private String title;
    private Long typeId;
    private boolean recommented;

    public BlogQuery() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public boolean isRecommented() {
        return recommented;
    }

    public void setRecommented(Boolean recommented) {
        this.recommented = recommented;
    }
}
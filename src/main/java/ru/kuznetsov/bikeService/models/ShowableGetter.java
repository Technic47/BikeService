package ru.kuznetsov.bikeService.models;

public class ShowableGetter {
    private String category;
    private Long id;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

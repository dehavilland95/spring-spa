package ru.volobuev.security.dto;

public class IdDTO {
    long id;
    public IdDTO() {}
    public IdDTO(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}

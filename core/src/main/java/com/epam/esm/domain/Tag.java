package com.epam.esm.domain;

public class Tag extends BaseDomain {
    private String name;

    public Tag() {
    }

    public Tag(long id, String name) {
        this.name = name;
        setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

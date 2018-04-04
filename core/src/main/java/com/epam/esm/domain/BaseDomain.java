package com.epam.esm.domain;

abstract class BaseDomain {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

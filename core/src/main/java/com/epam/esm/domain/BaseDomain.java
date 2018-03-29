package com.epam.esm.domain;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

abstract class BaseDomain {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseDomain that = (BaseDomain) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

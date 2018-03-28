package com.epam.esm.domain;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

abstract class BaseDomain {
    private static final AtomicLong counter = new AtomicLong();
    private long id = counter.getAndIncrement();

    public long getId() {
        return id;
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

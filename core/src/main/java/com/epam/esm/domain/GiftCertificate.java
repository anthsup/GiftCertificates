package com.epam.esm.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GiftCertificate extends BaseDomain {
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDate creationDate;
    private LocalDate lastModificationDate;
    private long durationInDays;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(LocalDate lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public long getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(long durationInDays) {
        this.durationInDays = durationInDays;
    }
}

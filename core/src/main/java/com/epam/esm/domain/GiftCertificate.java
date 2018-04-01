package com.epam.esm.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GiftCertificate {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDate creationDate;
    private LocalDate lastModificationDate;
    private long durationInDays;
    private List<Tag> tags;

    public GiftCertificate() {}

    private GiftCertificate(Builder builder) {
        id = builder.id;
        name = builder.name;
        description = builder.description;
        price = builder.price;
        creationDate = builder.creationDate;
        lastModificationDate = builder.lastModificationDate;
        durationInDays = builder.durationInDays;
        tags = builder.tags;
    }

    public static class Builder {
        private long id;
        private String name;
        private String description;
        private BigDecimal price;
        private LocalDate creationDate = LocalDate.now();
        private LocalDate lastModificationDate = LocalDate.now();
        private long durationInDays;
        private List<Tag> tags;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder creationDate(LocalDate creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder lastModificationDate(LocalDate lastModificationDate) {
            this.lastModificationDate = lastModificationDate;
            return this;
        }

        public Builder durationInDays(long durationInDays) {
            this.durationInDays = durationInDays;
            return this;
        }

        public Builder tags(List<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public GiftCertificate build() {
            return new GiftCertificate(this);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Tag> getTags() {
        if (tags != null) {
            return Collections.unmodifiableList(tags);
        }
        return null;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificate that = (GiftCertificate) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(price, that.price) &&
                Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, creationDate);
    }
}

package com.epam.esm.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class GiftCertificate extends BaseDomain {
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDateTime creationDate;
    private LocalDateTime lastModificationDate;
    private long durationInDays;
    private List<Tag> tags;

    public GiftCertificate() {
        creationDate = LocalDateTime.now();
        lastModificationDate = LocalDateTime.now();
    }

    private GiftCertificate(Builder builder) {
        this.setId(builder.id);
        name = builder.name;
        description = builder.description;
        price = builder.price;
        creationDate = builder.creationDate;
        lastModificationDate = builder.lastModificationDate;
        durationInDays = builder.durationInDays;
        tags = builder.tags;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public List<Tag> getTags() {
        if (tags != null) {
            return Collections.unmodifiableList(tags);
        }
        return Collections.emptyList();
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

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDateTime getLastModificationDate() {
        return lastModificationDate;
    }

    public static class Builder {
        private long id;
        private String name;
        private String description;
        private BigDecimal price;
        private LocalDateTime creationDate = LocalDateTime.now();
        private LocalDateTime lastModificationDate = LocalDateTime.now();
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

        public Builder creationDate(LocalDateTime creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder lastModificationDate(LocalDateTime lastModificationDate) {
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

    public long getDurationInDays() {
        return durationInDays;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}

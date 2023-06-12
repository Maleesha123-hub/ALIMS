package com.alims.londontech.entities;

import com.alims.londontech.utils.LocalDateTimeConverter;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class AuditData {

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createdOn;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime updatedOn;

    private Long createdBy;

    private Long updatedBy;

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
}

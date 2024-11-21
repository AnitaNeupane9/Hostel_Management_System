package com.codilien.hostelmanagement;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    protected LocalDateTime createdAt;

    @CreatedBy
    protected String createdBy;

    @LastModifiedDate
    protected LocalDateTime updatedAt;

    @LastModifiedBy
    protected String updatedBy;

// @CreatedBy, @CreatedDate, @LastModifiedBy, @LastModifiedDate -- JPA Auditing
}

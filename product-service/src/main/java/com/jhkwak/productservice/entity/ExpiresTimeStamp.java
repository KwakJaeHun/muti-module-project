package com.jhkwak.productservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter @Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class ExpiresTimeStamp {

    @Column(updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime expiresAt;
}

package mintos.homework.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    
    public static final String DEFAULT_SCHEMA = "mintos";  

    @Id
    @GeneratedValue
    UUID id;

    @Version
    @ColumnDefault("0")
    Long version;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    String createdByUser;

    @LastModifiedBy
    String updatedByUser;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime dateCreated;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime dateUpdated;

}

package mintos.homework.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client_accounts", schema = BaseEntity.DEFAULT_SCHEMA)
public class ClientAccount extends BaseEntity {

    @Column(nullable = false)
    UUID clientId;

    @Column(nullable = false)
    String currency;

    @Column(nullable = false)
    BigDecimal balance;
    
    @Column
    @ColumnDefault("false")
    boolean deleted;

}

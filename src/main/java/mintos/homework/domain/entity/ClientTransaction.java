package mintos.homework.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mintos.homework.domain.TransactionStatus;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client_transactions", schema = BaseEntity.DEFAULT_SCHEMA)
public class ClientTransaction extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private ClientAccount account;

    @ManyToOne
    @JoinColumn(name = "target_account_id", nullable = false)
    private ClientAccount targetAccount;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

}

package mintos.homework.domain.repository;

import mintos.homework.domain.entity.ClientTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClientTransactionRepository extends JpaRepository<ClientTransaction, UUID> {
    
    List<ClientTransaction> findAllByAccountId(UUID accountId, Pageable pageable);

}

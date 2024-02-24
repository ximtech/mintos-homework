package mintos.homework.domain.repository;

import mintos.homework.domain.entity.ClientAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientAccountRepository extends JpaRepository<ClientAccount, UUID> {

    List<ClientAccount> findAllByClientId(UUID clientId);

}

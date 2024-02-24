package mintos.homework.service;

import lombok.RequiredArgsConstructor;
import mintos.homework.domain.entity.ClientAccount;
import mintos.homework.domain.repository.ClientAccountRepository;
import mintos.homework.dto.ClientAccountDTO;
import mintos.homework.exception.ApiException;
import mintos.homework.mapper.ClientAccountMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientAccountService {
    
    final ClientAccountRepository clientAccountRepository;
    final ClientAccountMapper clientAccountMapper;
    
    public List<ClientAccountDTO> listClientAccounts(UUID clientId) {
        return clientAccountRepository.findAllByClientId(clientId).stream()
                .map(clientAccountMapper::mapToClientAccount)
                .collect(Collectors.toList());
    }
    
    public ClientAccount findClientAccount(UUID accountId) {
        return clientAccountRepository.findById(accountId)
                .orElseThrow(() -> new ApiException("Account with id: [%s] do not exist".formatted(accountId), HttpStatus.NOT_FOUND));
    }
    
    public void saveClientAccount(ClientAccount account) {
        clientAccountRepository.save(account);
    }
}

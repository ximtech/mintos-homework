package mintos.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mintos.homework.domain.TransactionStatus;
import mintos.homework.domain.entity.ClientAccount;
import mintos.homework.domain.entity.ClientTransaction;
import mintos.homework.domain.repository.ClientTransactionRepository;
import mintos.homework.dto.ClientTransactionDTO;
import mintos.homework.dto.CurrencyExchangeRateDTO;
import mintos.homework.dto.FundTransactionRequestDTO;
import mintos.homework.dto.FundTransactionResponseDTO;
import mintos.homework.exception.ApiException;
import mintos.homework.mapper.ClientTransactionMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientTransactionService {
    
    final ClientAccountService clientAccountService;
    final CurrencyExchangeRateService currencyExchangeRateService;
    final ClientTransactionRepository clientTransactionRepository;
    final ClientTransactionMapper clientTransactionMapper;
    
    public List<ClientTransactionDTO> listClientTransactions(UUID accountId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        return clientTransactionRepository.findAllByAccountId(accountId, pageable).stream()
                .map(clientTransactionMapper::mapToTransaction)
                .collect(Collectors.toList());
    }

    @Transactional
    public FundTransactionResponseDTO executeClientFundTransaction(FundTransactionRequestDTO transactionRequest) {
        ClientAccount fromClientAccount = clientAccountService.findClientAccount(transactionRequest.fromAccountId());
        ClientAccount toClientAccount = clientAccountService.findClientAccount(transactionRequest.toAccountId());
        if (!toClientAccount.getCurrency().equalsIgnoreCase(transactionRequest.currency())) {
            String message = "Invalid target currency. Can't transfer [%s] to [%s] account".formatted(transactionRequest.currency().toUpperCase(), toClientAccount.getCurrency());
            throw new ApiException(message, HttpStatus.BAD_REQUEST);
        }

        ClientTransaction fundTransaction = createClientTransaction(transactionRequest, fromClientAccount, toClientAccount);
        updateFundTransactionStatus(fundTransaction, TransactionStatus.IN_PROGRESS);
        
        BigDecimal amountToTransfer = convertAmount(toClientAccount.getCurrency(), fromClientAccount.getCurrency(), transactionRequest.amount());
        if (fromClientAccount.getBalance().compareTo(amountToTransfer) < 0) {
            updateFundTransactionStatus(fundTransaction, TransactionStatus.REJECTED_INSUFFICIENT_FUNDS);
            throw new ApiException("Not enough funds to proceed", HttpStatus.BAD_REQUEST);
        }
        
        fromClientAccount.setBalance(fromClientAccount.getBalance().subtract(amountToTransfer));
        toClientAccount.setBalance(toClientAccount.getBalance().add(amountToTransfer));
        clientAccountService.saveClientAccount(fromClientAccount);
        clientAccountService.saveClientAccount(toClientAccount);

        updateFundTransactionStatus(fundTransaction, TransactionStatus.COMPLETED);
        return new FundTransactionResponseDTO("Funds successfully transferred");
    }
    
    private ClientTransaction createClientTransaction(FundTransactionRequestDTO transactionRequest, ClientAccount fromClientAccount, ClientAccount toClientAccount) {
        ClientTransaction entity = new ClientTransaction();
        entity.setAccount(fromClientAccount);
        entity.setTargetAccount(toClientAccount);
        entity.setAmount(transactionRequest.amount());
        entity.setCurrency(transactionRequest.currency());
        return entity;
    }
    
    private void updateFundTransactionStatus(ClientTransaction fundTransaction, TransactionStatus status) {
        fundTransaction.setStatus(status);
        clientTransactionRepository.save(fundTransaction);
    }

    private BigDecimal convertAmount(String fromCurrency, String toCurrency, BigDecimal amount) {
        if (fromCurrency.equalsIgnoreCase(toCurrency)) {
            return amount;
        }
        CurrencyExchangeRateDTO rateDto = currencyExchangeRateService.getCurrencyExchangeRate(fromCurrency);
        return Optional.ofNullable(rateDto.rateMap().get(toCurrency))
                .map(amount::multiply)
                .orElseThrow(() -> new ApiException("Exchange rate not available for currency: [%s]".formatted(toCurrency), HttpStatus.NOT_FOUND));
    }
}

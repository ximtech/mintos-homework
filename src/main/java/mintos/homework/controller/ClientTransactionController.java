package mintos.homework.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mintos.homework.dto.ClientAccountDTO;
import mintos.homework.dto.ClientTransactionDTO;
import mintos.homework.dto.FundTransactionRequestDTO;
import mintos.homework.dto.FundTransactionResponseDTO;
import mintos.homework.exception.ApiException;
import mintos.homework.service.ClientAccountService;
import mintos.homework.service.ClientTransactionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/client/transactions")
@RequiredArgsConstructor
public class ClientTransactionController {

    final ClientTransactionService clientTransactionService;

    @GetMapping(path = "{accountId}")
    public Mono<List<ClientTransactionDTO>> listClientTransactions(@PathVariable("accountId") UUID accountId,
                                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                                            @RequestParam(name = "size", defaultValue = "50") int size,
                                                            @RequestParam(name = "sortBy", defaultValue = "dateCreated") String sortBy) {
        log.debug("GET /api/client/transactions [{}]", accountId);
        return Mono.fromCallable(() -> clientTransactionService.listClientTransactions(accountId, page, size, sortBy))
                .publishOn(Schedulers.boundedElastic());
    }

    @PostMapping("/execute/fund")
    @CircuitBreaker(name = "currency-api-service")
    @TimeLimiter(name = "currency-api-service")
    @RateLimiter(name = "currency-api-service")
    public Mono<FundTransactionResponseDTO> executeFundTransaction(@Valid @RequestBody FundTransactionRequestDTO request) {
        log.debug("GET /api/client/transactions [{}]", request);
        return Mono.fromCallable(() -> clientTransactionService.executeClientFundTransaction(request))
                .publishOn(Schedulers.boundedElastic());
    }

}

package mintos.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mintos.homework.dto.ClientAccountDTO;
import mintos.homework.service.ClientAccountService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/client/accounts")
@RequiredArgsConstructor
public class ClientAccountController {
    
    final ClientAccountService clientAccountService;

    @GetMapping(path = "{clientId}")
    public Mono<List<ClientAccountDTO>> listClientAccounts(@PathVariable("clientId") UUID clientId) {
        log.debug("GET /api/client/accounts [{}]", clientId);
        return Mono.fromCallable(() -> clientAccountService.listClientAccounts(clientId))
                .publishOn(Schedulers.boundedElastic());
    }

}

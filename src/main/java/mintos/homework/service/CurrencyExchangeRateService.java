package mintos.homework.service;

import lombok.RequiredArgsConstructor;
import mintos.homework.dto.CurrencyExchangeRateDTO;
import mintos.homework.exception.ApiException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeRateService {
    
    @Qualifier("exchangeRateClient")
    final RestClient restClient;

    @Cacheable(cacheNames = "currencyExchangeRateCache")
    public CurrencyExchangeRateDTO getCurrencyExchangeRate(String currency) {
        return Optional.ofNullable(restClient.get()
                .uri("/latest/%s".formatted(currency.toUpperCase()))
                .retrieve()
                .toEntity(CurrencyExchangeRateDTO.class)
                .getBody())
                .filter((CurrencyExchangeRateDTO rate) -> rate.result().equals("success"))
                .orElseThrow(() -> new ApiException("Unable to receive exchange rates from exchange rate API", HttpStatus.SERVICE_UNAVAILABLE));
    }
}

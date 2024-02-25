package mintos.homework.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ApplicationCircuitBreakerConfig {

    public static final String CIRCUIT_BREAKER_NAME = "currency-api-service";

    @Bean
    CircuitBreaker apiServiceCircuitBreaker(CircuitBreakerRegistry registry) {
        log.info("Registering circuit breaker");
        return registry.circuitBreaker(CIRCUIT_BREAKER_NAME);
    }

    @Bean
    Retry apiServiceRetry(RetryRegistry registry) {
        log.info("Registering retry circuit breaker");
        return registry.retry(CIRCUIT_BREAKER_NAME);
    }

    @Bean
    TimeLimiter apiServiceTimeLimiter(TimeLimiterRegistry registry) {
        log.info("Registering time limiter circuit breaker");
        return registry.timeLimiter(CIRCUIT_BREAKER_NAME);
    }

}

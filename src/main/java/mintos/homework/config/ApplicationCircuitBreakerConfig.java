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
    
    @Bean
    CircuitBreaker apiServiceCircuitBreaker(CircuitBreakerRegistry registry) {
        log.info("Registering circuit breaker");
        return registry.circuitBreaker("currency-api-service");
    }

    @Bean
    Retry apiServiceRetry(RetryRegistry registry) {
        log.info("Registering retry circuit breaker");
        return registry.retry("currency-api-service");
    }

    @Bean
    TimeLimiter apiServiceTimeLimiter(TimeLimiterRegistry registry) {
        log.info("Registering time limiter circuit breaker");
        return registry.timeLimiter("currency-api-service");
    }

}

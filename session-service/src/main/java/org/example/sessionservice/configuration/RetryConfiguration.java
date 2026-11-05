package org.example.sessionservice.configuration;

import org.example.sessionservice.exception.NotFoundException;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RetryConfiguration {

    @Bean
    public Retry userServiceRetry() {

        RetryConfig retryConfig = RetryConfig.custom()
                .intervalFunction(
                        IntervalFunction.ofExponentialBackoff(1000, 2.0)
                )
                .maxAttempts(3)
                .ignoreExceptions(NotFoundException.class)
                .build();

        RetryRegistry retryRegistry = RetryRegistry.of(retryConfig);

        return retryRegistry.retry("userServiceRetry");
    }
}
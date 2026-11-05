package org.example.sessionservice.service.impl;

import io.github.resilience4j.retry.Retry;
import org.example.sessionservice.service.UserClientService;
import org.example.sessionservice.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpClientErrorException;

import java.util.function.Supplier;

@Service
public class UserClientServiceImpl implements UserClientService {

    private final RestTemplate userServiceRestTemplate;
    private final Retry userServiceRetry;

    public UserClientServiceImpl(RestTemplate restTemplate, Retry userServiceRetry) {
        this.userServiceRestTemplate = restTemplate;
        this.userServiceRetry = userServiceRetry;
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        Supplier<UserResponseDto> supplier = Retry.decorateSupplier(userServiceRetry, () ->
                userServiceRestTemplate.getForObject("/user/" + userId, UserResponseDto.class)
        );
        try {
            return supplier.get();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Ne mogu da dohvatim korisnika sa ID " + userId, e);
        }
    }

    @Override
    public void incrementSessionsApplied(Long userId) {
        Supplier<Void> supplier = Retry.decorateSupplier(userServiceRetry, () -> {
            userServiceRestTemplate.postForObject( "/user/" + userId + "/increment-sessions-applied", null, Void.class);
            return null;
        });
        supplier.get();
    }

    @Override
    public void updateAttendance(Long userId, boolean attended) {
        Supplier<Void> supplier = Retry.decorateSupplier(userServiceRetry, () -> {
            userServiceRestTemplate.postForObject(  "/user/" + userId + "/update-attendance?attended=" + attended, null, Void.class);
            return null;
        });
        supplier.get();
    }

    @Override
    public void incrementOrganizedSessions(Long userId) {
        Supplier<Void> supplier = Retry.decorateSupplier(userServiceRetry, () -> {
            userServiceRestTemplate.postForObject( "/user/" + userId + "/increment-organized-sessions", null, Void.class);
            return null;
        });
        supplier.get();
    }

    @Override
    public void decrementSessionsApplied(Long userId) {
        Supplier<Void> supplier = Retry.decorateSupplier(userServiceRetry, () -> {
            userServiceRestTemplate.postForObject(
                    "/user/" + userId + "/decrement-sessions-applied",
                    null,
                    Void.class
            );
            return null;
        });
        supplier.get();
    }
}
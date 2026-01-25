package com.home;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserAuthClient {

    private final WebClient webClient;

    public UserAuthClient(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public AuthPayload login(String email, String password) {

        AuthRequest request = new AuthRequest();
        request.setEmail(email);
        request.setPassword(password);

        return webClient.post()
                .uri("http://localhost:8001/api/login") //  Consul service name
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AuthPayload.class)
                .block();
    }
}

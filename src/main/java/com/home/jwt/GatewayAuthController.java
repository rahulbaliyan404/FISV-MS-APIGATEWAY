package com.home.jwt;

import com.home.AuthPayload;
import com.home.AuthRequest;
import com.home.UserAuthClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class GatewayAuthController {

    private final UserAuthClient userAuthClient;
    private final JwtUtil jwtUtil;

    public GatewayAuthController(UserAuthClient userAuthClient,
                                 JwtUtil jwtUtil) {
        this.userAuthClient = userAuthClient;
        this.jwtUtil = jwtUtil;
    }

@PostMapping("/authenticate")
public Mono<ResponseEntity<Map<String, Object>>> authenticate(
        @RequestBody AuthRequest request) {

 return Mono.fromCallable(() -> userAuthClient.login(request.getEmail(), request.getPassword()))
         .subscribeOn(Schedulers.boundedElastic())
         .map(user -> {
             String token = jwtUtil.generateToken(user);
             return ResponseEntity.ok(Map.of(
                     "id_token", token,
                     "expiresIn", 3600
             ));
         });
    }
}

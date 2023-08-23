package com.example.practicesecurity.controller;

import com.example.practicesecurity.utils.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class HelloController {

    @PostMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok().body("token");
    }

    @Value("${jwt.token.secret}")
    private String key;

    private Long expireTimeMs = 1000 * 60 * 60l;


    @PostMapping("/gettoken")
    public ResponseEntity<String> getToken() {
        String token = JwtProvider.createToken("userName", key, expireTimeMs);
        return ResponseEntity.ok(token);
    }


}

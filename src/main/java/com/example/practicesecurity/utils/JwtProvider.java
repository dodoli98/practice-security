package com.example.practicesecurity.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtProvider {

    public static String createToken(String userName, String key, long expireTimeMs) {
        Date now = new Date(System.currentTimeMillis());

        Claims claims = Jwts.claims();
        claims.put("user_name", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))  // 토큰 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs)) // 토큰 만료시간
                .signWith(SignatureAlgorithm.HS256, key)  // 알고리즘 설정
                .compact();

    }


}

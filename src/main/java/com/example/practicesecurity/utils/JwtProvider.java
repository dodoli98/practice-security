package com.example.practicesecurity.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
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

    //토큰에서 userName 꺼내오는 메서드
    public static String getUserName(String token, String key) {
        return extractClaims(token, key).get("userName").toString();
    }

    private static Claims extractClaims(String token, String key) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    //토큰 만료확인 메서드
    public static boolean isExpired(String token, String secretkey) {
        Date expiredDate = extractClaims(token, secretkey).getExpiration();
        return expiredDate.before(new Date());
    }

}
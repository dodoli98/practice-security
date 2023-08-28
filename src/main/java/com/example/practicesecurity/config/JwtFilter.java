package com.example.practicesecurity.config;

import com.example.practicesecurity.utils.JwtProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    @Value("${jwt.token.secret}")
    private String key;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. 헤더에서 토큰을 추출
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorizationHeader: {}", authorizationHeader);

        // 2. Authorization 헤더가 없거나 "Bearer "로 시작하지 않으면 다음 필터로 넘김
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 토큰을 "Bearer "로 분리하여 실제 토큰 부분을 얻어옴
        String token;
        try {
            token = authorizationHeader.split(" ")[1];
        } catch (Exception e) {
            log.error("토큰 추출에 실패했습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // 4. 토큰이 만료 되었는지 Check
        if (JwtProvider.isExpired(token, key)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 5. token에서 userName 꺼내기
        String userName = JwtProvider.getUserName(token, key);
        log.info("사용자 이름: {}", userName);

        // 6. 사용자 정보로 UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userName, null);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 7. SecurityContextHolder에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}

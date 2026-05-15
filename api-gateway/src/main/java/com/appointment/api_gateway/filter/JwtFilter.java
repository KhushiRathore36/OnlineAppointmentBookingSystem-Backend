package com.appointment.api_gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JwtFilter implements GlobalFilter {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        HttpMethod method = exchange.getRequest().getMethod();

        System.out.println("Gateway path = " + path);
        System.out.println("Gateway method = " + method);

        if (isPublicPath(path)) {
            System.out.println("Public endpoint hit, skipping JWT filter");
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No token found");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String role = claims.get("role", String.class);
            System.out.println("Role from token = " + role);

            
            if (path.startsWith("/providers")
                    && method == HttpMethod.DELETE
                    && !"ADMIN".equals(role)) {
                return forbidden(exchange);
            }

         
            if (path.startsWith("/providers")
                    && method != HttpMethod.DELETE
                    && !"ADMIN".equals(role)
                    && !"PROVIDER".equals(role)) {
                return forbidden(exchange);
            }

            
            if (path.startsWith("/records")
                    && !"PROVIDER".equals(role)) {
                return forbidden(exchange);
            }

        
            if (path.startsWith("/reviews")
                    && !"PATIENT".equals(role)) {
                return forbidden(exchange);
            }

        } catch (Exception e) {
            e.printStackTrace();
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/auth/login")
                || path.startsWith("/auth/register")
                || path.startsWith("/auth/oauth-role")
                || path.startsWith("/oauth2/")
                || path.startsWith("/login/oauth2/");
    }

    private Mono<Void> forbidden(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }
}
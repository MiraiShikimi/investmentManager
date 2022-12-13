package com.csgoinvestmentmanager.investmentManager.filer;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {


    private final String key;

    public CustomAuthorizationFilter(String key) {
        this.key = key;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info(request.getServletPath() +  "   requested for authorization");
        if(request.getServletPath().equals("/login") || request.getServletPath().equals("/api/auth/refresh/token")
                || request.getServletPath().equals("/api/auth/signup") || request.getServletPath().equals("/swagger-ui") || request.getServletPath().equals("/sw.js")
                || request.getServletPath().equals("/favicon.ico")) {
            log.info("skiping authorization");
            filterChain.doFilter(request,response);
        } else {

            String authorizationHeader = request.getHeader(AUTHORIZATION);
            log.info("checking the auth token");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256(key.getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null,authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                }catch (Exception exception){
                    log.error("Error logggin in");
                    response.setHeader("error", exception.getMessage());
                    response.setStatus(FORBIDDEN.value());
                   // response.sendError(FORBIDDEN.value());

                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", exception.getMessage());

                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
                 } else {
                log.error("MISSING AUTHENTICATION TOKEN");
                filterChain.doFilter(request, response);
            }
        }


    }
}

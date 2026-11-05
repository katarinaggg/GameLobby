package org.example.sessionservice.security;

import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.sessionservice.security.service.TokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Configuration
public class SecurityAspect {

    private final TokenService tokenService;

    public SecurityAspect(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Around("@annotation(org.example.sessionservice.security.CheckSecurity)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String token = null;

        // trazimo authorization header
        for (int i = 0; i < signature.getParameterNames().length; i++) {
            if (signature.getParameterNames()[i].equals("authorization")) {
                String authHeader = joinPoint.getArgs()[i].toString();
                if (authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                }
            }
        }

        if (token == null) { // nema tokena
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Claims claims = tokenService.parseToken(token); // parsiramo JWT
        if (claims == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Long userId = claims.get("id", Long.class);
        String role = claims.get("role", String.class);

        SecurityContext.setUserId(userId);

        CheckSecurity checkSecurity = method.getAnnotation(CheckSecurity.class);

        if (Arrays.asList(checkSecurity.roles()).contains(role)) {
            try {
                return joinPoint.proceed();
            } finally {
                SecurityContext.clear();
            }
        }

        SecurityContext.clear();
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
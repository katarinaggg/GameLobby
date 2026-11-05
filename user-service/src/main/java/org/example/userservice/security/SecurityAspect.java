package org.example.userservice.security;

import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.userservice.security.service.TokenService;
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

    @Around("@annotation(org.example.userservice.security.CheckSecurity)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        System.out.println(Arrays.toString(
                ((MethodSignature) joinPoint.getSignature()).getParameterNames()
        ));


        String token = null;
        for (int i = 0; i < signature.getParameterNames().length; i++) {
            if (signature.getParameterNames()[i].equals("authorization")) {
                // provera headera
                String authHeader = joinPoint.getArgs()[i].toString();
                if (authHeader.startsWith("Bearer ")) {
                    token = authHeader.split(" ")[1];
                }
            }
        }

        // unauthorized ako nema token
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // parsiranje tokena
        Claims claims = tokenService.parseToken(token);
        if (claims == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // cuvamo u "local storage" id kada se uloguje
        Long userId = claims.get("id", Long.class);
        SecurityContext.setUserId(userId);

        CheckSecurity checkSecurity = method.getAnnotation(CheckSecurity.class);
        String role = claims.get("role", String.class);

        // ako ima dobru ulogu vracamo procesuiramo
        if (Arrays.asList(checkSecurity.roles()).contains(role)) {
            try {
                return joinPoint.proceed();
            } finally {
                SecurityContext.clear();
            }
        }

        // ako nema odgovarajucu ulogu
        SecurityContext.clear();
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}

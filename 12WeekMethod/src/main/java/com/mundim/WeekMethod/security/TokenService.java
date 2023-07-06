package com.mundim.WeekMethod.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mundim.WeekMethod.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    public String generateToken(User user){
        return JWT.create()
                .withIssuer("User")
                .withSubject(user.getEmail())
                .withClaim("id", user.getId())
                .withExpiresAt(LocalDateTime.now()
                        .plusMinutes(30)
                        .toInstant(ZoneOffset.of("-03:00"))
                ).sign(Algorithm.HMAC256("secret"));
    }

    public String getSubject(String token){
        return JWT.require(Algorithm.HMAC256("secret"))
                .withIssuer("User")
                .build().verify(token).getSubject();
    }

}

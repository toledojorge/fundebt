/**
 * Code licensed under Attribution-NonCommercial-ShareAlike 4.0 International license
 * 
 * @author: Carlos Molero Mata
 * 
 * Copyrighted from 2022 - Present
 */

package dev.carlosmolero.fundebt.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import dev.carlosmolero.fundebt.config.CustomConfig;

@Component
public class JWTUtils {
        private static final String issuer = "Fundebt";

        @Autowired
        private CustomConfig customConfig;

        private Algorithm accessTokenAlgorithm;
        private JWTVerifier accessTokenVerifier;

        @PostConstruct
        public void init() {
                accessTokenAlgorithm = Algorithm.HMAC512(customConfig.getAccessTokenSecret());
                accessTokenVerifier = JWT.require(accessTokenAlgorithm).withIssuer(issuer).build();
        }

        public String generateAccessToken(Long userId) {
                return JWT.create().withIssuer(issuer).withSubject(String.valueOf(userId)).withIssuedAt(new Date())
                                .withExpiresAt(Date.from(Instant.now().plus(customConfig.getAccessTokenExpirationDays(),
                                                ChronoUnit.DAYS)))
                                .sign(accessTokenAlgorithm);
        }

        private Optional<DecodedJWT> decodeAccessToken(String token) {
                return Optional.of(accessTokenVerifier.verify(token));
        }

        public boolean validateAccessToken(String token) {
                return decodeAccessToken(token).isPresent();
        }

        public String getUserIdFromAccessToken(String token) {
                return decodeAccessToken(token).get().getSubject();
        }

}

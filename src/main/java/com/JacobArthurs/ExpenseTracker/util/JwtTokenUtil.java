package com.JacobArthurs.ExpenseTracker.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtTokenUtil {
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String SECRET_KEY = "%t$upm@4XU^*eXBU88Rg&v8%8VSj7CP9&M3Snt7DLRSkaA2iTG";

    public static String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        try {
            return Jwts.builder()
                    .issuer("Expense Tracker")
                    .subject(username)
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(getSecretKey())
                    .compact();
        }
        catch (Exception e) {
            return null;
        }

    }

    public static boolean validateToken(String token) {
        try {
            var a = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();

                    return a.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public static String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private static SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
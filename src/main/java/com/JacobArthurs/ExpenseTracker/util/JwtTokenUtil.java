package com.JacobArthurs.ExpenseTracker.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtTokenUtil {
    // Token expiration time: 10 days (in milliseconds)
    public static final long EXPIRATION_TIME = 864_000_000;
    // Secret key used for signing the token
    public static final String SECRET_KEY = "%t$upm@4XU^*eXBU88Rg&v8%8VSj7CP9&M3Snt7DLRSkaA2iTG";

    /**
     * Generates a JWT token for the given username.
     *
     * @param username The username to include in the token
     * @return The generated JWT token
     */
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

    /**
     * Validates the given JWT token.
     *
     * @param token The token to validate
     * @return True if the token is valid and not expired, false otherwise
     */
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

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token The token from which to extract the username
     * @return The username extracted from the token
     */
    public static String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Retrieves the SecretKey used for signing the JWT token.
     *
     * @return The SecretKey instance
     */
    private static SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}

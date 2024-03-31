package com.aruiz.user.notification.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${token.secret.key}")
    String jwtSecretKey;

    @Value("${token.expirationms}")
    Long jwtExpirationMs;

    public String extractUserName(String token) {
        return extractClain(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T extractClain(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims) // Establece las reclamaciones adicionales en el token
                .setSubject(userDetails.getUsername()) // Establece el email del usuario como sujeto del token
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Establece la fecha de emisión del token como la fecha actual
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Establece la fecha de expiración del token
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Firma el token con la clave de firma utilizando el algoritmo HS256
                .compact(); // Compacta el token en su forma final
    }

    private boolean isTokenExpired(String token) {
        // Compara la fecha de expiración del token con la fecha actual
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        // Extrae la fecha de expiración del token JWT utilizando el resolver de reclamaciones
        return extractClain(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        // Construye un analizador de tokens JWT, configura la clave de firma y extrae las reclamaciones del cuerpo del token
        return Jwts
                .parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    private Key getSigningKey() {
        // Decodifica la clave secreta del token desde Base64 y la utiliza para crear una clave HMAC-SHA para firmar y verificar tokens JWT
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

package com.smartcart.userservice.security;

import com.smartcart.userservice.models.Role;
import com.smartcart.userservice.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public Key getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    //claim is key-value pair data type data structure (HashMap)
    public String generateToken(User user){
        return Jwts.builder().          //for building jwt token it builds
                setSubject(user.getEmail()). //Identifies WHO this token belongs to // Usually email or username
                claim("userId", user.getId()).
                 claim("roles",user.getRoles().    // Collection of Role entities  //claim is custom for storing any piece of info
                stream().
                map(Role::getName).                    // Convert Role → "ROLE_USER", "ROLE_ADMIN"
                toList()).                      // Convert stream → List<String>
                setIssuedAt(new Date()).           // Time when token was created
                setExpiration(new Date(System.currentTimeMillis()+expiration)).   // Token will become INVALID after this time
                signWith(getSecretKey(), SignatureAlgorithm.HS256).   // Signs the token using:Secret key (HMAC key) & HS256 algorithm (HMAC SHA-256)
                compact();       //Converts header + payload + signature into final JWT string: xxxxx.yyyyy.zzzzz
    }

    //claim is key-value pair data type data structure (HashMap)
    public Long validateToken(String token){
        Claims claim= Jwts.parser()
                .setSigningKey(getSecretKey())
                .parseClaimsJws(token)//validation happens here
                .getBody();
        //return claim.get("userId", Long.class);
        return Long.valueOf(String.valueOf(claim.get("userId")));
    }
}

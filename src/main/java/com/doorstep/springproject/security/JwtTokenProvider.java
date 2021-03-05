package com.doorstep.springproject.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Austin Oyugi
 * @since 3/4/2021
 * @email austinoyugi@gmail.com
 */

@Component
public class JwtTokenProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication)
    {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime()+jwtExpirationInMs);

        Claims claims = Jwts.claims().setSubject(String.valueOf(userPrincipal.getId()));

//        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
//
//        for (GrantedAuthority simpleGrantedAuthority : userPrincipal.getAuthorities()){
//            grantedAuthorityList.add(new SimpleGrantedAuthority(simpleGrantedAuthority.getAuthority()));
//        }
//
//        claims.put("auth", grantedAuthorityList);

        return Jwts.builder()
                .setSubject(userPrincipal.getId())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserIdFromJwt(String token)
    {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken)
    {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;

        }catch (SignatureException ex)
        {
            LOGGER.error("Invalid JWT signature");

        }catch (MalformedJwtException ex)
        {
            LOGGER.error("Invalid JWT token");

        }catch (ExpiredJwtException ex)
        {
            LOGGER.error("Expired JWT token");

        }catch (IllegalArgumentException ex)
        {
            LOGGER.error("JWT claims string is empty");
        }

        return false;
    }
}

package com.ecommerce.product_service.security;

import java.util.Date;

import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${jwt.secret}")
	private String secretKey;
	
	@Value("${jwt.access-token-expiry}")
	private long accessTokenExpiry;
	
	@Value("${jwt.refresh-token-expiry}")
	private long refreshTokenExpiry;
	
	
	
	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	
	
	
    public String generateAccessToken(UserDetails userDetails) {
    	
    	return Jwts.builder()
    			.subject(userDetails.getUsername())
    			.claim("role",userDetails.getAuthorities().stream().findFirst().get().getAuthority())
    			// used email as the subject of the token generation
    			.issuedAt(new Date())
    			.expiration(new Date(System.currentTimeMillis() + accessTokenExpiry))
    			.signWith(getSigningKey())
    			.compact();
    	
    
		}

    public String generateRefreshToken(UserDetails userDetails) {
		return Jwts.builder()
				.subject(userDetails.getUsername()) // used email as the token generation
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis()+refreshTokenExpiry))
				.signWith(getSigningKey())
				.compact();
		}

    //Generic claim retriever
    
    public <T> T extractClaim(String token , Function<Claims,T> claimResolver) {
    	Claims claims = extractAllClaims(token);
    	return claimResolver.apply(claims);
    }
    
    
    
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String extractRole(String token) {
    	return extractClaim(token,claims -> claims.get("role",String.class));
    }
        
    public String extractEmail(String token) {
		return  extractClaim(token,Claims::getSubject);
		}

    // Validate token — check email matches AND token not expired
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }





}

package org.library.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil
{

	@Value("${jwt.expiration}")
	private long expiration;

	@Value("${jwt.secret}")
	private String secretKey;

	//extractClaim,isTokenExpired,isTokenValid,extractUsername
	public String generateToken(UserDetails userDetails)
	{
		return Jwts.builder().subject(userDetails.getUsername()).expiration(new Date(System.currentTimeMillis() + expiration)).issuedAt(new Date())
				.signWith(getSigningKey()).compact();
	}

	private SecretKey getSigningKey()
	{
		byte[] key = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(key);
	}

	public String extractUserName(String token)
	{
		return this.extractClaim(token, Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimResolver)
	{
		Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
		return claimResolver.apply(claims);
	}

	public boolean isTokenValid(String token, UserDetails userDetails)
	{
		String email = extractUserName(token);
		return userDetails.getUsername().equals(email) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token)
	{
		Date expiration = extractClaim(token, Claims::getExpiration);
		return expiration.before(new Date());
	}
}

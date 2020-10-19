package org.bizpay.common.util;

import java.util.Date;

import java.util.logging.Level;

import org.bizpay.exception.KeyErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.java.Log;
@Log
@Component
@PropertySource("classpath:auth.properties")
public class JwtUtil {
	@Value("${jwt.secret}")
	private String jwtSecret;
	
	@Value("${token.type}")
	private String tokenType;
	
	@Value("${token.issuer}")
	private String issuer;
	
	@Value("${token.audience}")
	private String audience;
	
	@Value("${token.expiry.sec}")
	private int exSec;
	
	@Value("${token.expiry.minute}")
	private int exMinute;
	
	@Value("${token.expiry.hour}")
	private int exHour;
	
	public String getJwtKey(String userId) throws Exception {
		log.info("인증키 발행");

		byte[] signingKey = jwtSecret.getBytes();
		//Date expiraTime = new Date(System.currentTimeMillis() + (exSec *exMinute*exHour ) );
		Date expiraTime = new Date(System.currentTimeMillis() + (3600000 ) );
		log.info(expiraTime.toString());
		String token = Jwts.builder()
				.signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS256)
				.setHeaderParam("typ", tokenType)
				.setIssuer(issuer)
				.setAudience(audience)
				.setSubject(userId)
				.setExpiration(expiraTime)
				.compact();
		return token;
	}
	
	public boolean parsedKey(String key) {
		
		try {
			byte[] signingKey = jwtSecret.getBytes();
			Jws<Claims> parsedToken = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(key.replace("Bearer ", ""));

			String userId = parsedToken
                    .getBody()
                    .getSubject();
			log.info(userId);
			
		}catch(ExpiredJwtException e) {
			log.log (Level.SEVERE, e.getLocalizedMessage());
			throw new KeyErrorException("인증시간만료");
		}catch(MalformedJwtException e) {
			log.log (Level.SEVERE, e.getLocalizedMessage());
			throw new KeyErrorException("인증키에 문제가 있습니다.");
		} 
		catch(JwtException e) {
			log.log (Level.SEVERE, e.getLocalizedMessage());
			throw new KeyErrorException("위변조 시도");
		}
		catch (Exception e) {
			// 각 예외처리 추가예정
			e.printStackTrace();
			log.log (Level.SEVERE, e.getMessage());
			throw new KeyErrorException("인증에 오류가 있습니다.");
	
			
		}
		return true;
	}
	
	// 키에 포홤된 아이디와 사용자 아이디가 동일한지 체크하는 기능
	public boolean parsedKey(String key , String userId) {
		try {
			byte[] signingKey = jwtSecret.getBytes();
			Jws<Claims> parsedToken = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(key.replace("Bearer ", ""));

			String userIdToken = parsedToken
                    .getBody()
                    .getSubject();
			if(userIdToken.equals(userId)) {
				return true;
			}else return false;
			
		}
		catch (Exception e) {
			// 각 예외처리 추가예정
			e.printStackTrace();
			log.log (Level.SEVERE, e.getMessage());
			return false;
		}

	}
}

package com.example.onboarding.jwtTest;

import static org.junit.jupiter.api.Assertions.*;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.example.onboarding.auth.JwtUtil;
import com.example.onboarding.enums.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")  // application-test.properties 파일 로드
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtUtilTest {

	@Autowired
	private JwtUtil jwtUtil;
	private String token;
	private Key key;
	private Long userId = 1L;
	private String username = "testUser";
	private String nickname = "testNickname";
	private UserRole userRole = UserRole.ROLE_USER;
	@Value("${jwt.secret.key}")
	private String secretKey;

	@BeforeEach
	public void setUp() {
		byte[] bytes = Base64.getDecoder()
			.decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
		token = jwtUtil.createAccessToken(userId, username, nickname, userRole);
	}

	@Test
	void testCreateAccessToken() {
		assertNotNull(token, "Access token should not be null");
		assertTrue(token.startsWith("Bearer "), "Token should start with Bearer prefix");
	}

	@Test
	void testSubstringToken() {
		String extractedToken = jwtUtil.substringToken(token);
		System.out.println(extractedToken);
		assertEquals(token.substring(7), extractedToken,
			"Extracted token should match the original token without Bearer prefix");
	}

	@Test
	void testGetUserInfoFromToken() {
		System.out.println("token = " + token);
		token = jwtUtil.substringToken(token);
		Claims claims = jwtUtil.getUserInfoFromToken(token);
		assertEquals(userId.toString(), claims.getSubject(), "User ID from token should match");
		assertEquals(username, claims.get("username"), "Username from token should match");
		assertEquals(userRole.toString(), claims.get("userRole"), "User role from token should match");
		assertEquals(nickname, claims.get("nickname"), "Nickname from token should match");
	}

	@Test
	void testValidateToken() {
		token = jwtUtil.substringToken(token);
		boolean isValid = jwtUtil.validateToken(token);
		assertTrue(isValid, "Token should be valid");

		// 만료된 토큰 생성
		String expiredToken = Jwts.builder()
			.setSubject("expiredUser")
			.setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 10)) // 10분 전 발급
			.setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 5)) // 5분 전 만료
			.signWith(key, SignatureAlgorithm.HS256) // 비밀 키 사용
			.compact();

		boolean isExpiredTokenValid = jwtUtil.validateToken(expiredToken);
		assertFalse(isExpiredTokenValid, "Expired token should not be valid");
	}

	@Test
	void testValidateTokenWithInvalidToken() {
		String invalidToken = "invalidToken";
		boolean isValid = jwtUtil.validateToken(invalidToken);
		assertFalse(isValid, "Invalid token should return false");
	}

}
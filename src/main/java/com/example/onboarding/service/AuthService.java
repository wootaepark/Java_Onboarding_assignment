package com.example.onboarding.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.onboarding.auth.JwtUtil;
import com.example.onboarding.dto.req.SigninReqDto;
import com.example.onboarding.dto.req.SignupReqDto;
import com.example.onboarding.dto.res.SigninResDto;
import com.example.onboarding.dto.res.SignupResDto;
import com.example.onboarding.entity.User;
import com.example.onboarding.enums.UserRole;
import com.example.onboarding.repository.UserRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Transactional
	public SignupResDto signup(SignupReqDto signupRequest) {

		if (userRepository.existsByUsername(signupRequest.getUsername())) {
			throw new IllegalArgumentException("이미 존재하는 username 입니다.");
		}

		String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

		User newUser = User.builder()
			.username(signupRequest.getUsername())
			.password(encodedPassword)
			.nickname(signupRequest.getNickname())
			.role(UserRole.ROLE_USER)
			.build();

		User savedUser = userRepository.save(newUser);

		return new SignupResDto(savedUser.getUsername(), savedUser.getNickname(), newUser.getRole());
	}

	public SigninResDto signin(SigninReqDto signinRequest) {
		log.info("서비스 시작");
		User user = userRepository.findByUsername(signinRequest.getUsername()).orElseThrow(
			() -> new IllegalArgumentException("가입되지 않은 유저입니다."));

		// 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환합니다.
		if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
		}

		String bearerToken = jwtUtil.createAccessToken(user.getId(), user.getUsername(), user.getNickname(),
			user.getRole());

		System.out.println("bearerToken : " + bearerToken);

		return new SigninResDto(bearerToken);
	}

	public String refreshToken(String token) {
		Claims claims = jwtUtil.getUserInfoFromToken(token);
		Long userId = Long.valueOf(claims.getSubject());
		if (jwtUtil.validateToken(token)) {
			return jwtUtil.createRefreshToken(userId);
		} else
			throw new IllegalArgumentException("refresh token not valid");
	}
}
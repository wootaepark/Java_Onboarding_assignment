package com.example.onboarding.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.onboarding.dto.req.SigninReqDto;
import com.example.onboarding.dto.req.SignupReqDto;
import com.example.onboarding.dto.res.SigninResDto;
import com.example.onboarding.dto.res.SignupResDto;
import com.example.onboarding.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/auth/signup")
	public SignupResDto signup(@RequestBody SignupReqDto signupRequest) {
		return authService.signup(signupRequest);
	}

	@PostMapping("/auth/signin")
	public ResponseEntity<SigninResDto> signin(@RequestBody SigninReqDto signinRequest) {
		return ResponseEntity.status(HttpStatus.OK).body(authService.signin(signinRequest));
	}

	@GetMapping("/refresh")
	public ResponseEntity<String> refreshToken(
		@RequestHeader("Authorization") String refreshToken
	) {
		log.info(refreshToken);
		log.info("refreshToken 으로 토큰 갱신");
		return ResponseEntity.status(201).body(authService.refreshToken(refreshToken));

	}

	// 인증, 인가 필터가 정상적으로 동작하는지 확인하기 위한 컨트롤러 
	@GetMapping("/test")
	public String test() {
		return "test";

	}
}

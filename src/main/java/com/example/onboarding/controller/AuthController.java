package com.example.onboarding.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.onboarding.dto.req.SigninReqDto;
import com.example.onboarding.dto.req.SignupReqDto;
import com.example.onboarding.dto.res.SigninResDto;
import com.example.onboarding.dto.res.SignupResDto;
import com.example.onboarding.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/auth/signup")
	public SignupResDto signup(@RequestBody SignupReqDto signupRequest) {
		return authService.signup(signupRequest);
	}

	@PostMapping("/auth/signin")
	public SigninResDto signin(@RequestBody SigninReqDto signinRequest) {
		return authService.signin(signinRequest);
	}

	// 인증, 인가 필터가 정상적으로 동작하는지 확인하기 위한 컨트롤러 
	@GetMapping("/test")
	public String test() {
		return "test";

	}
}

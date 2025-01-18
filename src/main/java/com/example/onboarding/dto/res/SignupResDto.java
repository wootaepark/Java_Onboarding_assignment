package com.example.onboarding.dto.res;

import com.example.onboarding.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResDto {
	private String username;
	private String nickname;
	private UserRole authorities;
	
}

package com.example.onboarding.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SigninResDto {
	private final String token;

	@Builder
	public SigninResDto(String accessToken) {
		this.token = accessToken;
	}
}

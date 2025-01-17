package com.example.onboarding.enums;

import java.util.Arrays;

import com.sun.jdi.request.InvalidRequestStateException;

public enum UserRole {
	ROLE_ADMIN,
	ROLE_USER;

	public static UserRole of(String role) {
		return Arrays.stream(UserRole.values())
			.filter(r -> r.name().equalsIgnoreCase(role))
			.findFirst()
			.orElseThrow(() -> new InvalidRequestStateException("유효하지 않은 UerRole"));
	}
}

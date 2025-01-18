package com.example.onboarding.entity;

import com.example.onboarding.enums.UserRole;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String password;
	private String nickname;
	@Enumerated(EnumType.STRING)
	private UserRole role;

	@Builder
	public User(String username, String password, String nickname, UserRole role) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.role = role;
	}
}

package com.example.onboarding.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.onboarding.entity.User;
import com.example.onboarding.enums.UserRole;
import com.example.onboarding.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		User user = User.builder()
			.username("taewoo")
			.role(UserRole.ROLE_USER)
			.nickname("nick1")
			.build();
		userRepository.save(user);
	}

	@Test
	@WithCustomMockUser
	public void IfUserExistsThenGetUserInfoReturnsSuccess() throws Exception {
		mockMvc.perform(get("/test")
				.header("X-AUTH-TOKEN", "aaaaaaa"))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
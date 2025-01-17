package com.example.onboarding.auth;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.onboarding.dto.req.SigninReqDto;
import com.example.onboarding.enums.UserRole;
import com.example.onboarding.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
		//setFilterProcessesUrl("/auth/signin");
		// 위 주석 해제 시 header 에 토큰 부여
		// 이후 컨트롤러 실행 안됨

	}

	// 토큰 생성 후 아이디 패스워드를 담아서 메니저에 전달
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
		HttpServletResponse response) throws
		AuthenticationException {
		try {
			SigninReqDto signinRequest = new ObjectMapper().readValue(request.getInputStream(), SigninReqDto.class);
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword());
			return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
			// 여기서 토큰은 JWT 가 아니라 인증 요청을 보내기 위한 토큰이다.
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {
		log.info("로그인 성공 및 JWT 생성");
		Long userId = ((UserDetailsImpl)authResult.getPrincipal()).getUser().getId();
		String username = ((UserDetailsImpl)authResult.getPrincipal()).getUser().getUsername();
		String nickname = ((UserDetailsImpl)authResult.getPrincipal()).getUsername();
		UserRole role = ((UserDetailsImpl)authResult.getPrincipal()).getUser().getRole();
		String token = jwtUtil.createToken(userId, username, nickname, role);
		response.setHeader("Authorization", token);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException,
		ServletException {
		log.info("로그인 실패: {}", failed.getMessage());
		response.setStatus(401);
	}
}
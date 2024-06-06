package com.mycompany.let_ffle.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;

// 클라이언트에서 넘어온 Access Token을 받아 유효성 검증하는 필터
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private LetffleUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String accessToken = null;

		// 요청 헤더에서 Access Token 얻기
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String headerValue = httpServletRequest.getHeader("Authorization");
		if (headerValue != null && headerValue.startsWith("Bearer")) {
			accessToken = headerValue.substring(7);
			log.info(accessToken);
		}

		// Query String으로 전달된 Access Token 얻기
		// <img src="/board/battach/1?accessToken=xxx...>
		if (accessToken == null) {
			// 파라미터로 accessToken이 넘어온 경우 그 값을 accessToken에 넘겨줌
			if (request.getParameter("accessToken") != null) {
				accessToken = request.getParameter("accessToken");
			}
		}

		if (accessToken != null) {
			// Access Token 유효성 검사
			Jws<Claims> jws = jwtProvider.validateToken(accessToken);
			if (jws != null) {
				// 유효한 경우
				log.info("Access Token이 유효함");
				String userId = jwtProvider.getUserId(jws);
				log.info("userId : " + userId);

				// 사용자 상세 정보 얻기
				LetffleUserDetails userDetails = (LetffleUserDetails) userDetailsService.loadUserByUsername(userId);
				// 인증 객체 얻기
				Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());
				// 스프링 시큐리티에 인증 객체 설정
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				// 유효하지 않은 경우
				log.info("Access Token이 유효하지 않음");
			}
		}

		// 다음 필터를 실행 (반드시 넣어줘야 함)
		filterChain.doFilter(request, response);
	}

}

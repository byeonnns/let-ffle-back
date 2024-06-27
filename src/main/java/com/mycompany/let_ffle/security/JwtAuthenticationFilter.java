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
@Component // 관리 객체로 등록해줘야 하지만 이 파일은 Controller도 아니고 Service도 아님 -> Component 어노테이션으로 관리 객체 등록
public class JwtAuthenticationFilter extends OncePerRequestFilter { // OncePerRequestFilter를 상속받는 이유 : 2번 실행되는 오류를 방지하고자 1번만 실행하게끔 만들어진 필터를 상속받음
	// JwtProvider 객체를 주입받음 -> JwtProvider가 가지고 있는 메소드를 사용하기 위함
	@Autowired
	private JwtProvider jwtProvider;
	// LetffleUserDetailsService 객체를 주입받음 -> LetffleUserDetailsService가 가지고 있는 메소드를 사용하기 위함
	@Autowired
	private LetffleUserDetailsService userDetailsService;
	// 구현한 OncePerRequestFilter 인터페이스의 추상 메소드를 재정의
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String accessToken = null;
		
		// 이름이 Authorization인 헤더의 값을 저장
		String headerValue = request.getHeader("Authorization");
		// Authorization 헤더 값이 null이 아니고, Bearer로 시작한다면,
		if (headerValue != null && headerValue.startsWith("Bearer")) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
			// "Bearer "를 제거하고 실제 Access Token 값만 저장
			accessToken = headerValue.substring(7);
			log.info(accessToken);
		}

		// Access Token이 Query String으로 전달된 경우
		// <img src="/board/battach/1?accessToken=xxx...>
		if (accessToken == null) {
			// 파라미터로 accessToken이 넘어온 경우 그 값을 accessToken에 넘겨줌
			if (request.getParameter("accessToken") != null) {
				accessToken = request.getParameter("accessToken");
			}
		}

		// Access Token이 null이 아니라면 어떤 값이 실려들어왔다는 것
		if (accessToken != null) {
			// 그 Access Token의 유효성을 검사
			Jws<Claims> jws = jwtProvider.validateToken(accessToken);
			// jws가 null이 아니다 -> 해당 Access Token이 유효하다 (로그인 가능하다) 
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

		// 다음 필터를 실행 (반드시 넣어줘야 함) -> 그냥 Spring의 작동 구조이므로 나도 잘 모름..
		filterChain.doFilter(request, response);
	}

}

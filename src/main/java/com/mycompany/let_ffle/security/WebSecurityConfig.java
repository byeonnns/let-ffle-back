package com.mycompany.let_ffle.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableMethodSecurity(securedEnabled = true) // @Secured 어노테이션을 사용할 수 있도록 설정
public class WebSecurityConfig {
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	// @Bean : 인증 필터 체인을 관리 객체로 등록
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// REST API는 로그인 form을 제공하지 않으므로 form을 통한 로그인 인증을 사용하지 않도록 설정
		// 로그인 form은 front-end에서 제공해야 함
		http.formLogin(config -> config.disable());

		// 로그아웃은 토큰 보유 여부로 판단 -> 필요 없는 설정

		// HttpSession을 사용하지 않도록 설정 -> 우리 프로젝트는 Session이 아닌 JWT로 인증을 처리할 것이므로
		http.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// 사이트 간 요청 위조 방지 비활성화 (GET 이외 요청은 csrf 토큰을 요구함)
		http.csrf(config -> config.disable());

		// CORS 설정 (다른 도메인에서 받은 인증 정보(Access Token)로 요청할 경우 허가)
		http.cors(config -> {
		});

		// JWT로 인증이 되도록 필터 등록
		// 아이디와 패스워드를 체크하는 필터 앞에 두는 것이 좋음 (체크 전에 인증되면 바로 허가해줘야 하므로)
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	/*
	 * // 인증 관리자를 관리 객체로 등록
	 * 
	 * @Bean public AuthenticationManager
	 * authenticationManager(AuthenticationConfiguration
	 * authenticationConfiguration) throws Exception { return
	 * authenticationConfiguration.getAuthenticationManager(); }
	 */

	// 권한 계층을 관리 객체로 등록
	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
		hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
		return hierarchy;
	}

	// @PreAuthorize 어노테이션의 표현식을 해석하는 객체 등록
	@Bean
	public MethodSecurityExpressionHandler createExpressionHandler() {
		DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
		handler.setRoleHierarchy(roleHierarchy());
		return handler;
	}

	// 다른 도메인(크로스 도메인) 제한 설정
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// 요청 사이트 제한
		configuration.addAllowedOrigin("*"); // origin : 처음 인증 받은 곳

		// 요청 방식 제한
		configuration.addAllowedMethod("*");

		// 요청 헤더 제한
		configuration.addAllowedHeader("*");

		// 모든 URL 요청에 대해서 위 내용을 적용
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}

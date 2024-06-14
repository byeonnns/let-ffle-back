package com.mycompany.let_ffle.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component // 관리 객체로 등록해줘야 하지만 이 파일은 Controller도 아니고 Service도 아님 -> Component 어노테이션으로 관리 객체 등록
public class JwtProvider {
	
	// 서명 및 암호화에 사용되는 SecretKey 객체를 필드로 가짐
	private SecretKey secretKey;
	
	// Access Token의 유효 기간을 밀리초로 설정
	private final long accessTokenDuration = 24 * 60 * 60 * 1000;


	// 생성자 @Value 외부 설정 파일에서 정의된 값을 간편하게 주입 받아 사용할 수 있다.

	// 생성자 선언
	public JwtProvider(@Value("${jwt.security.key}") String jwtSecurityKey) {
		log.info(jwtSecurityKey);
		try {
			// application.properties에서 jwt.security.key의 값을 가져와서 그걸 기반으로 SecreyKey를 생성
			secretKey = Keys.hmacShaKeyFor(jwtSecurityKey.getBytes("UTF-8"));
		} catch (Exception e) {
			log.info(e.toString());
		}
	}

	// AccessToken을 실제로 생성하는 메소드
	public String createAccessToken(String userId, String authority) {
		
		// 일단 token 문자열을 담을 변수를 선언
		String token = null;
		
		try {
			// JSON Web Token은 헤더, 페이로드, 시그니처 구조로 이루어짐 -> 각각 들어갈 내용을 설정해주는 부분
			JwtBuilder builder = Jwts.builder();
			
			// header는 암호화에 사용된 알고리즘과 타입을 명시하는 곳 -> JwtBuilder를 사용하면 자동으로 설정해는 곳이므로 별도 코드가 필요 없음

			// payload 설정
			// 1) 토큰을 발급해준 대상의 ID를 페이로드 내용으로 적어줌 ex) "sub" : "tjdwns3823"
			builder.subject(userId);
			// 2) 토큰을 발급해준 대상의 권한을 페이로드 내용으로 적어줌 ex) "authority" : "ROLE_USER"
			builder.claim("authority", authority);
			// 3) 토큰의 유효기간을 페이로드 내용으로 적어줌 ex) "expiration" : "86400000" -> 24시간을 밀리초로 바꾼 값
			builder.expiration(new Date(new Date().getTime() + accessTokenDuration));

			// signature 설정
			// application.properties에서 jwt.security.key의 값을 이용해 만든 secretKey로 서명 -> 내가 발급해준건지 확인할 때 여길 보면 됨
			builder.signWith(secretKey);
			// 설정 다 했으면 compact() 호출 -> 실제 토큰이 다 만들어짐
			token = builder.compact();
			
		} catch (Exception e) {
			log.info(e.toString());
		}
		// 설정 다 된 토큰을 리턴
		return token;
	}

	// 토큰의 유효성을 검사하는 메소드
	public Jws<Claims> validateToken(String token) {
		// JWS는 Json Web Signature의 약자
		Jws<Claims> jws = null;
		try {
			// JWT의 유효성을 검증할 때 사용하는 ParserBuilder 객체를 생성
			JwtParserBuilder builder = Jwts.parser();
			// ParserBuilder에 내가 서명을 암호화할 때 사용한 secretKey를 알려줌 -> 이따 토큰값 주고 내가 서명한거 맞는지 검사해달라고 하기 위함
			builder.verifyWith(secretKey);
			// 유효성 검사를 시작하라고 시킴
			JwtParser parser = builder.build();
			// 매개변수로 입력받은 token 값 주고 검사한 결과를 jws에 저장
			jws = parser.parseSignedClaims(token);
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			// 오류가 발생했다 -> 매개변수로 입력받은 token이 유효성 검사를 통과하지 못했다
			// 이유 1. 매개변수로 입력받은 token이 내가 발행해준 토큰이 아닐 수 있음
			log.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			// 이유 2. 매개변수로 입력받은 token이 내가 발행해주긴 했는데 유효기간이 지났을 수 있음
			log.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			// 이유 3. 매개변수로 입력받은 token이 다른 곳에서 발급받은 토큰일 수 있음
			log.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			// 이유 4. 애초에 JWT 토큰 구조로 작성되지 않은 문자열일 수 있음 
			log.info("JWT 토큰이 잘못되었습니다.");
		}
		return jws;
	}

	// 토큰을 이용해 유저의 ID를 얻어내는 메소드
	public String getUserId(Jws<Claims> jws) {
		// 매개변수로 전달받은 토큰에서 Payload에 작성된 내용을 얻어냄
		Claims claims = jws.getPayload();
		// Payload에 작성된 내용에서 사용자 아이디 얻기
		String userId = claims.getSubject();
		return userId;
	}

	// 토큰을 이용해 유저의 권한을 얻어내는 메소드
	public String getAuthority(Jws<Claims> jws) {
		// 매개변수로 전달받은 토큰에서 Payload에 작성된 내용을 얻어냄
		Claims claims = jws.getPayload();
		// Payload에 작성된 내용에서 사용자 권한 얻기
		String autority = claims.get("authority").toString();
		return autority;
	}

	/*
	// 아래 Main 메소드를 실행시키면 AccessToken을 실제 발급해줌 -> 토큰이 제대로 발급되는지 확인하기 위해 작성한 메소드이므로 주석 처리
	public static final void main(String[] args) {
		JwtProvider jwtProvider = new JwtProvider("com.mycompany.jsonwebtoken.kosacourse");

		String accessToken = jwtProvider.createAccessToken("user", "ROLE_USER");
		log.info("AccessToken: " + accessToken);

		Jws<Claims> jws = jwtProvider.validateToken(accessToken);
		log.info("validate: " + ((jws != null) ? true : false));

		if (jws != null) {
			String userId = jwtProvider.getUserId(jws);
			log.info("userId: " + userId);

			String autority = jwtProvider.getAuthority(jws);
			log.info("autority: " + autority);
		}
	}
	*/
}
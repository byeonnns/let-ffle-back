package com.mycompany.let_ffle.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.mycompany.let_ffle.dto.Member;

public class LetffleUserDetails extends User {
	private Member member;

	// Member DTO를 매개변수로 입력받아서 부모 생성자 호출 -> 내가 갖고 있는 Member 필드에 해당 값을 저장
	public LetffleUserDetails(Member member, List<GrantedAuthority> authorities) {
		// super() : 부모의 생성자 호출
		super(member.getMid(), member.getMpassword(), member.isMenabled(), true, true, true, authorities);
		this.member = member;
	}

	// 필드로 갖고 있는 member 객체를 리턴
	public Member getMember() {
		return member;
	}
}
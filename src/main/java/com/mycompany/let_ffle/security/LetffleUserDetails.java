package com.mycompany.let_ffle.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.mycompany.let_ffle.dto.Member;

public class LetffleUserDetails extends User {
	private Member member;

	public LetffleUserDetails(Member member, List<GrantedAuthority> authorities) {
		super(member.getMid(), member.getMpassword(), member.isMenabled(), true, true, true, authorities);
		this.member = member;
	}

	public Member getMember() {
		return member;
	}
}
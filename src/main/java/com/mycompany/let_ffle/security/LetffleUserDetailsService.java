package com.mycompany.let_ffle.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mycompany.let_ffle.dao.MemberDao;
import com.mycompany.let_ffle.dto.Member;

@Service
public class LetffleUserDetailsService implements UserDetailsService {
	@Autowired
	private MemberDao memberDao;

	@Override
	public LetffleUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberDao.selectByMid(username);

		if (member == null) {
			throw new UsernameNotFoundException(username);
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(member.getMrole()));

		LetffleUserDetails userDetails = new LetffleUserDetails(member, authorities);
		return userDetails;
	}
}

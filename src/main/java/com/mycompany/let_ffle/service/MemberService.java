package com.mycompany.let_ffle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.let_ffle.dao.MemberDao;
import com.mycompany.let_ffle.dto.Member;
import com.mycompany.let_ffle.dto.RaffleDetail;

@Service
public class MemberService {
	@Autowired
	private MemberDao memberDao;

	public void join(Member member) {
		
		memberDao.insert(member);
		// join이라는 변수명을 준 후 MemberDto에서 있는 값을, Memberdao에 insert라는 변수의 담아져 있는 member를 가져와라 라는 의미이다.
	}

}

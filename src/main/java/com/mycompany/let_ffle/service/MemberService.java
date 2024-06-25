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
		// join이라는 변수명을 준 후 MemberDto에서 있는 값을, Memberdao에 insert라는 변수의 담아져 있는 member를
		// 가져와라 라는 의미이다.
	}

	public Member selectByMid(String mid) {

		Member member = memberDao.selectByMid(mid);

		return member;
	}

	// 회원 탈퇴
	public void deleteByMid(String mid) {
		// TODO Auto-generated method stub

		memberDao.delete(mid);
	}

	// mypage에 휴대폰 번호 변경
	public void changeMphone(Member member) {
		// memberDao에서 changeMphone이라는 변수 명을 둔후 MemberDao에 changeMphone이라는 메소드에 있는
		// Member를 가져오라는 의미
		memberDao.changeMphone(member);

	}



}

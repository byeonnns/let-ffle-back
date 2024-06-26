package com.mycompany.let_ffle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.let_ffle.dao.InquiryDao;
import com.mycompany.let_ffle.dao.MemberDao;
import com.mycompany.let_ffle.dto.Inquiry;
import com.mycompany.let_ffle.dto.Member;
import com.mycompany.let_ffle.dto.Pager;
import com.mycompany.let_ffle.dto.RaffleDetail;

@Service
public class MemberService {
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private InquiryDao inquiryDao;

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
	public void updateMphone(Member member) {
		// memberDao에서 changeMphone이라는 변수 명을 둔후 MemberDao에 changeMphone이라는 메소드에 있는
		// Member를 가져오라는 의미
		memberDao.updateMphone(member);

	}

	public void updateMpassword(String mid, String mpassword) {
		// 받아온 유저의 아이디와 비밀번호를 가지고 memberDao를 호출햐여 데이터 베이스의 비밀번호를 변경하도록 함
		// 쉽게말해 유저의 아이디랑 비밀번호 줄테니까 해당 유저의 비밀번호를 바꿔라 ~
		memberDao.updateMpassword(mid, mpassword);
	}

	public void updateMaddress(String mid, String maddress, String mzipcode) {
		// 컨트롤러에서 받아온 로그인한 유저의 아이디와 주소를 매개변수로 받아 dao를 호출해 데이터베이스에서 주소를 변경하도록 함
		memberDao.updateMaddress(mid, maddress, mzipcode);
	}

	// 여기입둥
	public void login(Member member, String mid, String mpassword) {
		memberDao.login(member, mid, mpassword);
	}
	
	/* 1:1 문의 */
	public void insertInquiry(Inquiry inquiry) {
		inquiryDao.insertInquiry(inquiry);
	}
	public int getCount() {
		return inquiryDao.InquiryCount();
	}
	public List<Inquiry> getInquiryList(Pager pager) {
		return inquiryDao.selectByPage(pager);
	}

	public Inquiry getInquiry(int ino) {	
		return inquiryDao.readInquiry(ino);
	}
	public int updateInquiry(Inquiry inquiry) {
		return inquiryDao.updateInquiry(inquiry);
	}


	

}

package com.mycompany.let_ffle.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.let_ffle.dto.BerryHistory;
import com.mycompany.let_ffle.dto.Inquiry;
import com.mycompany.let_ffle.dto.LikeList;
import com.mycompany.let_ffle.dto.Member;
import com.mycompany.let_ffle.dto.RaffleDetail;
import com.mycompany.let_ffle.dto.Winner;
import com.mycompany.let_ffle.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/member")
public class MemberController {

	// member 서비스 주입
	@Autowired
	private MemberService memberService;

	// 로그인
	@PostMapping("/login")
	public Map<String, String> login(String mid, String mpassword) {

		return null;
	}

	// 회원가입
	@PostMapping("/join")
	public Member join(Member member) {

		// Member dto 매개변수로 사용 -> 해당 값들이 필드 값으로 자동 주입됨

		// 패스워드 암호화

		// 야 이런 요청 들어왔으니까 이거 처리해줘
		// 서비스가 이 값을 전달받아서 실제로 DB에 처리
		// 서비스가 성공을 했든 실패를 했든 뭘 처리함 -> 그 결과를 컨트롤러에 전달
		// 컨트롤러가 서비스가 리턴해준 결과를 바탕으로 응답을 생성
		// 그걸 JSON으로 사용자에게 반환

		return null;
	}

	// 마이페이지 -> 좋아요 목록 조회 메소드
	@GetMapping("/likeList")
	public LikeList getLikeList(String mid) {

		return null;
	}

	// 마이페이지 -> 응모내역 조회 메소드
	@GetMapping("/raffleEntryList")
	public List<RaffleDetail> getRaffleEntryList(String mid) {

		return null;
	}

	// 마이페이지 -> 당첨내역 조회 메소드
	@GetMapping("/winList")
	public List<Winner> getWinList(String mid) {

		return null;
	}

	// 마이페이지 -> 회원정보수정 메소드 (비밀번호, 휴대폰번호, 회원탈퇴)
	// 비밀번호 수정
	@PutMapping("/changePassword")
	public Member changePassword(String mid, String mpassword) {

		return null;
	}

	// 휴대폰 번호 수정
	@PutMapping("/chagnePhone")
	public Member changePhone(String mid, String mphone) {

		return null;
	}
	
	// 회원 탈퇴
	@PutMapping("/deleteMember")
	public Member deleteMember(String mid) {

		// 해당 Member 객체의 menabled 값을 false로 변환하는 코드

		return null;
	}

	// 문의 관련 CRUD 메소드
	// 문의 생성
	@PostMapping("/createInquiry")
	public Inquiry createInquiry(Inquiry inquiry) {
		
		return null;
	}
	
	// 문의 상세
	@GetMapping("/readInquiry")
	public Inquiry readInquiry(int ino) {
		
		return null;
	}
	
	// 문의 수정
	@PutMapping("/updateInquiry")
	public Inquiry updateInquiry(int ino) {
		
		return null;
	}
	
	// 문의 삭제
	@DeleteMapping("/deleteInquiry")
	public Inquiry deleteInquiry(int ino) {
		
		// 실제 DB에서 데이터 삭제를 해도 되는 기준 -> PK를 참조하고 있는 다른 테이블이 있는가?
		
		return null;
	}

	// BerryHistory 관련 메소드
	// 베리 사용내역 생성 메소드
	
	// 베리 사용내역 조회 메소드
	@GetMapping("/getBerryHistoryList")
	public List<BerryHistory> getBerryHistoryList(String mid) {
		
		return null;
	}
	

}

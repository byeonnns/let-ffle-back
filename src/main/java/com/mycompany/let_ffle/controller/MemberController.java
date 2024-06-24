package com.mycompany.let_ffle.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public Member join(@RequestBody Member member) {

		// 1. 비밀번호 암호화하기
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		member.setMpassword(passwordEncoder.encode(member.getMpassword()));

		// 아이디 활성화
		member.setMenabled(true);
		// 권한 설정
		member.setMrole("ROLE_USER");
		// 회원가입 시 베리 1개 제공
		member.setMberry(1);

		memberService.join(member);

		// 비밀번호 내역
		member.setMpassword(null);

		return member;

		// resquestBody 어노테이션을 사용한이유는 postman에서의 사용할때 body안에 쓰는 값때문에 사용이된것이다. 그게 아닌 하나의
		// 값을 가져올경우는 @requestParam String mid 이런 형식으로 작성하게 된다.
		// Member member를 사용한것은 memberDto에 있는 매개변수의 값들을 저장하기위하여 작성한것이다.
		// 그후 PasswordEncoder에 변수명을 passwordEncoder로 준후
		// PasswordEncoderFactories.createDelegatingPasswordEncoder(); 암호화를 위하여 사용했으며->
		// security에 사용되는 암호화 방법이다
		// member.setMapssword를 암호화 시켜서 DB에서 저장된 (asdfaewfa)암호를 Dto에서 불러 와라 라는 의미이다.
		// 여기서 setmpassword와 getmpassword의 차이는 set같은 경우는 우리가(보여지지 않겠끔)직접적으로 주어야되는 값들을
		// 이용할때 사용되고, get같은 경우는 사용자들이 직접입력된 값들(보여지는 것들)을 가져올때 사용한다.
		// 그렇게 하여 menable의 같은경우는 true를 주어 활성화를 설정 시키고, mrole은 권한으로 유저를 주게 된다.
		// setMberry같은 경우는 우리가 회원가입시에 1개식 주기로한 지정된 값이기 때문에 1로 주게 된다.
		// 등록을 할시 memberservice에 있는 join 이라는 변수명에 우리가 가져올 값을 선언해준다.
		// 그 후 비밀번호의 암호화를 하여 값을 불러오게 되면 보여지게끔 만들면 안되기 때문에 가져올 값을 선언후에 가져오면 비밀번호는 보여지지 않기
		// 위해 set을
		// 사용하여 비밀번호의 값을 null로 처리해준다.
		// 그 후 Member로 리턴을 해준다.

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

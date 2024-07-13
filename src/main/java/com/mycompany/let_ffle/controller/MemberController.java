package com.mycompany.let_ffle.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.let_ffle.dto.BerryHistory;
import com.mycompany.let_ffle.dto.Board;
import com.mycompany.let_ffle.dto.Inquiry;
import com.mycompany.let_ffle.dto.Member;
import com.mycompany.let_ffle.dto.Pager;
import com.mycompany.let_ffle.dto.Winner;
import com.mycompany.let_ffle.dto.request.RaffleDetailRequest;
import com.mycompany.let_ffle.dto.request.RaffleRequest;
import com.mycompany.let_ffle.security.JwtProvider;
import com.mycompany.let_ffle.security.LetffleUserDetails;
import com.mycompany.let_ffle.security.LetffleUserDetailsService;
import com.mycompany.let_ffle.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private LetffleUserDetailsService letffleUserDetailsService;

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private MemberService memberService;

	// 로그인
	@PostMapping("/login")
	public Map<String, String> login(String mid, String mpassword) {
		LetffleUserDetails letffleUserDetails = letffleUserDetailsService.loadUserByUsername(mid);
		
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		boolean passwordResult = passwordEncoder.matches(mpassword, letffleUserDetails.getMember().getMpassword());

		// JSON 응답을 생성해주기 위한 HashMap을 생성
		Map<String, String> map = new HashMap<>();

		if (passwordResult) {
			Authentication authentication = new UsernamePasswordAuthenticationToken(letffleUserDetails, null,
					letffleUserDetails.getAuthorities());
			/*
			 * SecurityContextHolder는 현재 인증된(로그인한) 사용자에 대한 세부 정보가 포함된 보안 컨텍스트를 보유하는 Spring
			 * Security에서 제공하는 유틸리티 클래스 getContext()는 현재 보안 컨텍스트를 검색
			 * setAuthentication(authentication)은 이전 단계에서 생성된 Authentication 객체를 현재 보안 컨텍스트로
			 * 설정
			 */
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// JSON Web Token 생성
			String accessToken = jwtProvider.createAccessToken(mid, letffleUserDetails.getMember().getMrole());
			String mrole = letffleUserDetails.getAuthorities().iterator().next().toString();
			
			// 사용자에게 반환해줄 응답을 Map에 추가
			map.put("result", "success");
			map.put("mid", mid);
			map.put("mrole", mrole);
			map.put("accessToken", accessToken);

			Member member = memberService.selectLoginTime(mid);

		} else {
			// 비밀번호가 일치하지 않은 경우 (false를 리턴받은 경우)
			map.put("result", "로그인 실패");
		}

		return map;
	}
	
	@GetMapping("/mypage/main")
	public Map<String, String> getMypage(Authentication authentication) {
		Member member = memberService.selectByMid(authentication.getName());

		Map<String, String> map = new HashMap<>();

		map.put("mnickname", member.getMnickname());

		return map;
	}

	// 회원가입
	@PostMapping("/join")
	public Member join(@RequestBody Member member) {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		member.setMpassword(passwordEncoder.encode(member.getMpassword()));
		member.setMenabled(true);
		member.setMrole("ROLE_USER");
		member.setMberry(1);

		memberService.join(member);

		// JSON 응답에서는 비밀번호가 보이지 않도록 null로 값을 바꿔줌
		member.setMpassword(null);

		return member;
	}
	
	// 관리자 - 전체 회원 조회 및 페이지네이션
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/getAdminMemberList")
	public Map<String, Object> getAdminMemberList(@RequestParam(defaultValue = "1") int pageNo, 
			@RequestParam(defaultValue = "") String searchType, @RequestParam(defaultValue = "") String word) {
		int totalRows = 0;
		
		if (!word.equals("")) {
			totalRows = memberService.getMemberCountByWord(searchType, word);
		} else {			
			totalRows = memberService.getMemberCount();
		}

		Pager pager = new Pager(20, 5, totalRows, pageNo);
		
		List<Member> list = memberService.getAdminMemberList(pager, searchType, word);
		
		Map<String, Object> map = new HashMap<>();
		map.put("member", list);
		map.put("pager", pager);

		return map;
	}

	// 아이디 중복 검사
	@PostMapping("/idDuplicationCheck/{mid}")
	public Map<String, String> idDuplicationCheck(@PathVariable String mid) {
		int count = memberService.countByMid(mid);

		Map<String, String> map = new HashMap<>();
		
		if (count == 1) {
			map.put("result", "fail");
		} else {
			map.put("result", "success");
		}

		return map;
	}

	// 닉네임 중복 검사
	@PostMapping("/nicknameDuplicationCheck/{mnickname}")
	public Map<String, String> nicknameDuplicationCheck(@PathVariable String mnickname) {
		int count = memberService.countByMnickname(mnickname);
		
		Map<String, String> map = new HashMap<>();

		if (count == 1) {
			map.put("result", "fail");
		} else {
			map.put("result", "success");
		}

		return map;
	}

	// 휴대폰 번호 중복 체크
	@PostMapping("/phoneDuplicationCheck/{mphone}")
	public Map<String, String> phoneDuplicationCheck(@PathVariable String mphone) {
		int count = memberService.countByMphone(mphone);
		Map<String, String> map = new HashMap<>();

		if (count == 1) {
			map.put("result", "fail");
		} else {
			map.put("result", "success");
		}

		return map;
	}
	
	@PostMapping("/mypage/passwordMatchCheck/{mpassword}")
	public Map<String, String> passwordMatchCheck(@PathVariable String mpassword, Authentication authentication) {
		Member member = memberService.selectByMid(authentication.getName());
		
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		boolean result = passwordEncoder.matches(mpassword, member.getMpassword());
		
		Map<String, String> map = new HashMap<>();

		if (result) {
			map.put("result", "success");
		} else {
			map.put("result", "fail");
		}

		return map;
	}

	// 아이디 찾기
	@PostMapping("/findId")
	public Map<String, Object> findId(@RequestBody Member member) {
		String mid = memberService.findId(member);

		Map<String, Object> map = new HashMap<>();

		if (mid != null) {
			map.put("result", "success");
			map.put("mid", mid);
		} else {
			map.put("result", "fail");
		}

		return map;
	}

	// 비밀번호 찾기
	@PostMapping("/findPassword/{mphone}/{mid}")
	public Map<String, Object> findPassword(@PathVariable String mphone, @PathVariable String mid) {
		int count = memberService.findPassword(mphone, mid);

		Map<String, Object> map = new HashMap<>();

		if (count == 1) {
			map.put("result", "success");
		} else {
			map.put("result", "fail");
		}

		return map;
	}
	
	// 비밀번호 재설정
	@PutMapping("/resetMpassword/{mid}/{mpassword}")
	public void resetMpassword(@PathVariable String mid, @PathVariable String mpassword) {
		LetffleUserDetails userDetails = letffleUserDetailsService.loadUserByUsername(mid);

		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		userDetails.getMember().setMpassword(passwordEncoder.encode(mpassword));

		memberService.updateMpassword(userDetails.getMember().getMid(), userDetails.getMember().getMpassword());
	}
	
	// 마이페이지 - 내 게시물 목록
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/myBoardList")
	public Map<String, Object> getMyBoardList(Authentication authentication,
			@RequestParam(defaultValue = "1") int pageNo) {
		int totalRows = memberService.getMyBoardCount(authentication.getName());
		
		Pager pager = new Pager(5, 5, totalRows, pageNo);

		List<Board> list = memberService.getMyBoardList(pager, authentication.getName());

		Map<String, Object> map = new HashMap<>();

		map.put("board", list);
		map.put("pager", pager);
		
		return map;
	}

	// 마이페이지 -> 좋아요 목록 조회 메소드
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/likeList")
	public Map<String, Object> getLikeList(@RequestParam(defaultValue = "1") int pageNo,
			Authentication authentication) {
		int totalRows = memberService.getLikeListCount(authentication.getName());
		
		Pager pager = new Pager(5, 5, totalRows, pageNo);
		
		List<RaffleRequest> list = memberService.getLikeList(pager, authentication.getName());
		
		Map<String, Object> map = new HashMap<>();
		map.put("RaffleRequest", list);
		map.put("pager", pager);
		
		return map;
	}
	
	//래플 좋아요 여부 확인 기능
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/likeStatus/{rno}")
	public boolean likeStatus(Authentication authentication, @PathVariable int rno) {
		return memberService.getLikeStatus(authentication.getName(), rno);
	}
	
	// 래플 관심 목록 추가 기능
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PostMapping("/addLikeList/{rno}")
	public void addLikeList(Authentication authentication, @PathVariable  int rno) {
		memberService.insertAddLikeList(authentication.getName(), rno);
	}

	// 래플 관심 목록 삭제 기능
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@DeleteMapping("/deleteLikeList/{rno}")
	public String deleteLikeList(Authentication authentication,@PathVariable int rno) {
		memberService.deleteLikeList(authentication.getName(), rno);
		return "관심 래플 삭제";
	}

	// 마이페이지 -> 내가 작성한 댓글 목록 조회
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/boardCommentList")
	public Map<String, Object> getBoardCommentList(@RequestParam(defaultValue = "1") int pageNo,
			Authentication authentication) {
		int totalRows = memberService.getBoardCommentCount(authentication.getName());
		
		Pager pager = new Pager(5, 5, totalRows, pageNo);
		
		List<Board> list = memberService.getBoardTitleList(pager, authentication.getName());
		
		Map<String, Object> map = new HashMap<>();
		map.put("total", list);
		map.put("pager", pager);
		
		return map;
	}

	// 마이페이지 - 비밀번호 수정
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PutMapping("/mypage/updateMpassword/{mpassword}")
	public void updateMpassword(Authentication authentication, @PathVariable String mpassword) {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		
		memberService.updateMpassword(authentication.getName(), passwordEncoder.encode(mpassword));
	}
	
	// 마이페이지 - 닉네임 수정
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PutMapping("/mypage/updateMnickname/{mnickname}")
	public void updateMnickname(@PathVariable String mnickname, Authentication authentication) {
		memberService.updateMnickname(authentication.getName(), mnickname);
	}

	// 마이페이지 - 휴대폰 번호 수정
	@PutMapping("/mypage/updateMphone/{mphone}")
	public void updateMphone(@PathVariable String mphone, Authentication authentication) {
		memberService.updateMphone(authentication.getName(), mphone);
	}

	// 마이페이지 - 주소 수정
	@PutMapping("/mypage/updateMaddress/{mzipcode}/{maddress}")
	public void updateMaddress(@PathVariable String mzipcode, @PathVariable String maddress, Authentication authentication) {
		memberService.updateMaddress(authentication.getName(), mzipcode, maddress);
	}

	// 회원 탈퇴
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PutMapping("/deleteMember")
	public void deleteMember(Authentication authentication) {
		memberService.deleteByMid(authentication.getName());
	}

	// 문의 등록
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PostMapping("/mypage/createInquiry")
	public Inquiry createInquiry(Inquiry inquiry, Authentication authentication) {
		inquiry.setMid(authentication.getName());
		
		// 첨부파일이 들어있는지 확인
		if (inquiry.getIattach() != null && !inquiry.getIattach().isEmpty()) {
			MultipartFile mf = inquiry.getIattach();
			inquiry.setIattachoname(mf.getOriginalFilename());
			inquiry.setIattachtype(mf.getContentType());
			try {
				inquiry.setIattachdata(mf.getBytes());
			} catch (IOException e) {
			}
		}

		memberService.insertInquiry(inquiry);

		// JSON으로 변환되지 않는 필드를 null 처리
		inquiry.setIattach(null);
		inquiry.setIattachdata(null);

		return inquiry;
	}

	// 문의 목록 가져오기 (추후 권한을 매개변수로 받아서 조건에 따라 다른 값을 리턴할 예정)
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/getInquiryList")
	// map 타입을 지정해주고 , @requestparam(defaultValu =1 ) 을 준 이유는 페이져를 할때 첫번째 페이지가
	// 1번이라는 것을 지정해주기 위해 1을 기본 값으로 준것이다.
	public Map<String, Object> getInquiryList(@RequestParam(defaultValue = "1") int pageNo, Authentication authentication) {
		int totalRows = memberService.getInquiryCount(authentication.getName());
		Pager pager = new Pager(5, 5, totalRows, pageNo);
		List<Inquiry> list = memberService.getInquiryList(pager, authentication.getName());
		Map<String, Object> map = new HashMap<>();
		map.put("inquiry", list);
		map.put("pager", pager);
		return map;
	}
	
	// 관리자 페이지 (회원 문의 목록 가져오기)
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/getUserInquiryList")
	public Map<String, Object> getUserInquiryList(@RequestParam(defaultValue = "1") int pageNo, Authentication authentication) {
		int totalRows = memberService.getUserInquiryCount(authentication.getName(), authentication.getAuthorities().iterator().next().toString());
		Pager pager = new Pager(5, 5, totalRows, pageNo);
		// 로그인한 유저의 이름과 유저의 권한을 얻어오기
		List<Inquiry> list = memberService.getUserInquiryList(pager);
		Map<String, Object> map = new HashMap<>();
		map.put("inquiry", list);
		map.put("pager", pager);
		return map;
	}
	
	// 문의 상세 보기
	@GetMapping("/inquiryDetail/{ino}")
	public Inquiry inquiryDetail(@PathVariable int ino) {
		Inquiry inquiry = memberService.readInquiry(ino);

		inquiry.setIattachdata(null);

		return inquiry;
	}

	// 문의 수정
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PutMapping("/updateInquiry")
	public Inquiry updateInquiry(Inquiry inquiry) {
		// 첨부파일이 포함되어 있는지 검사
		if (inquiry.getIattach() != null && !inquiry.getIattach().isEmpty()) {
			MultipartFile mf = inquiry.getIattach();
			inquiry.setIattachoname(mf.getOriginalFilename());
			inquiry.setIattachtype(mf.getContentType());

			try {
				inquiry.setIattachdata(mf.getBytes());
			} catch (IOException e) {
			}
		}

		memberService.updateInquiry(inquiry);

		// JSON으로 변환되지 않는 필드는 null 처리
		inquiry.setIattach(null);
		inquiry.setIattachdata(null);

		return inquiry;
	}
	
	// 문의 첨부 다운로드
	@GetMapping("/iattach/{ino}")
	public void iattach(@PathVariable int ino, HttpServletResponse response) {
		Inquiry inquiry = memberService.selectInquiryIno(ino);
		try {
			// 첨부파일 이름이 한글일 경우, 브라우저에서 한글 이름으로 다운로드 받기 위해 응답 헤더에 내용을 추가
			String fileName = new String(inquiry.getIattachoname().getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

			// 파일 타입을 헤더에 추가
			response.setContentType(inquiry.getIattachtype());
			log.info(inquiry.getIattachtype());

			// Response Body에 파일 데이터 출력
			OutputStream os = response.getOutputStream();
			os.write(inquiry.getIattachdata());
			os.flush();
			os.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	// 마이페이지 대시보드
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/getMyPageDashboard")
	public Map<String, Object> getMyPageDashboard(Authentication authentication){
		return memberService.getMyPageDashboard(authentication.getName());
	}

	// 베리 사용내역 조회
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/getBerryHistoryList")
	public Map<String, Object> getBerryHistoryList(Authentication authentication, @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "Total")String option) {
		return memberService.getBerryHistoryList(authentication.getName(), pageNo, option);
	}
	
	// 헤더에 나오는 베리 사용 내역 조회 (팝업용)
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/getBerryHistoryListForHome")
	public List<BerryHistory> getBerryHistoryListForHome(Authentication authentication) {
		List<BerryHistory> list = memberService.getBerryHistoryUpToTen(authentication.getName());
		return list;
	}

	// 문의 답변 작성
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PutMapping("/inquiryReply")
	public void inquiryReply(@RequestParam int ino, @RequestParam String ireply) {
		memberService.updateInquiryReply(ino, ireply);
	}
	
	@PutMapping("/updateWinner")
	public void updateWinner(Winner winner, Authentication authentication) {
		winner.setMid(authentication.getName());
		memberService.updateWinner(winner);
	}
	
	// 내 주소 가져오기
	@GetMapping("/getMyAddress")
	public Member getMyAddress(Authentication authentication) {
		return memberService.selectByMid(authentication.getName());
	}

	// 관리자 - 당첨자 조회
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/getAdminWinnerList")
	public Map<String, Object> getWinnerList(@RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "") String searchType, @RequestParam(defaultValue = "") String word) {
		int totalRows = 0;
		
		if (!word.equals("")) {
			totalRows = memberService.getWinnerCountByWord(searchType, word);
		} else {			
			totalRows = memberService.getWinnerCount();
		}

		Pager pager = new Pager(30, 5, totalRows, pageNo);
		
		List<RaffleDetailRequest> list = memberService.getAdminWinnerList(pager, searchType, word);
		
		Map<String, Object> map = new HashMap<>();
		map.put("winner", list);
		map.put("pager", pager);

		return map;
	}
	
	@GetMapping("/getMember")
	public Member getMember(Authentication authentication) {
		Member member = memberService.selectByMid(authentication.getName());
		
		if (member != null) {
			member.setMlastlogintime(null);
			member.setMpassword(null);
		}
		return member;
	}
	
	// 관리자 페이지 전체 회원 조회
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/getMemberDetail/{mid}")
	public Member getMemberDetail(@PathVariable String mid) {
		return memberService.getMember(mid);
	}
}

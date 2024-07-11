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

	// loadUserByUsername 메소드를 사용하여 유저 정보를 얻고 있으므로 해당 메소드를 가지고 있는
	// LetffleUserDetailsService를 주입받음
	@Autowired
	private LetffleUserDetailsService letffleUserDetailsService;

	// AccessToken을 만드는 메소드가 정의된 클래스가 JwtProvider -> 로그인 시 Access Token을 발급해줘야 하므로
	// 주입받음
	@Autowired
	private JwtProvider jwtProvider;

	// MemberService를 이용해 비즈니스 로직을 수행해야 하므로 주입받음
	@Autowired
	private MemberService memberService;

	// 로그인 처리
	@PostMapping("/login")
	public Map<String, String> login(String mid, String mpassword) {
		log.info("실행");
		log.info("mid :" + mid);
		log.info("mpassword :" + mpassword);
		// 주입받은 letffleUserDetailsService를 이용해 loadUserByUsername를 호출
		// 매개변수로 입력받은 mid를 전달
		// DB에 해당 mid를 가진 유저의 데이터를 LetffleUserDetails 객체의 Member 필드에 저장
		// getMember()로 데이터에 접근이 가능해짐
		LetffleUserDetails letffleUserDetails = letffleUserDetailsService.loadUserByUsername(mid);

		// 매개변수로 입력받은 mpassword는 암호화되지 않은 날것(raw)의 비밀번호
		// PasswordEncoder의 matches()를 사용해 DB에 있는 암호화된 비밀번호와 날것의 비밀번호를 비교 요청
		// 비밀번호가 같다면 true, 다르면 false를 리턴
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		boolean passwordResult = passwordEncoder.matches(mpassword, letffleUserDetails.getMember().getMpassword());

		// JSON 응답을 생성해주기 위한 HashMap을 생성
		Map<String, String> map = new HashMap<>();

		if (passwordResult) {
			// 비밀번호가 일치한 경우 (true를 리턴받은 경우)

			/*
			 * UsernamePasswordAuthenticationToken은 Authentication 인터페이스를 구현하는 클래스
			 * UsernamePasswordAuthenticationToken 생성자는 세 가지 매개변수가 필요 - letffleUserDetails:
			 * 사용자의 세부정보 (일반적으로 UserDetails를 구현하는 클래스의 객체) - null: 자격 증명을 나타냄. 앞으로 발급해줄
			 * Access Token을 이용해서 자격을 증명할 것이니 현재는 'null'로 설정 -
			 * letffleUserDetails.getAuthorities(): letffleUserDetails에서 사용자의 권한을 가져옴
			 */
			Authentication authentication = new UsernamePasswordAuthenticationToken(letffleUserDetails, null,
					letffleUserDetails.getAuthorities());
			/*
			 * SecurityContextHolder는 현재 인증된(로그인한) 사용자에 대한 세부 정보가 포함된 보안 컨텍스트를 보유하는 Spring
			 * Security에서 제공하는 유틸리티 클래스 getContext()는 현재 보안 컨텍스트를 검색
			 * setAuthentication(authentication)은 이전 단계에서 생성된 Authentication 객체를 현재 보안 컨텍스트로
			 * 설정
			 */
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// 로그인 처리를 위해 JWT를 생성
			// 주입받은 JwtProvider 객체가 가지고 있는 createAccessToken() 호출
			// 이 때 현재 로그인한 유저의 mid와 mrole을 제공
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
			// 로그인을 시켜주지 않고, 결과만 JSON 응답에 포함하여 반환
			map.put("result", "로그인 실패");
		}

		return map;
	}
	@PreAuthorize("hasAuthority('ROLE_USER')")
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
		log.info("member : " + member.toString());
		// 1. 비밀번호 암호화 -> PasswordEncoder의 encode() 사용
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		// 2. 암호화한 비밀번호를 Member DTO의 mpassword 필드값으로 세팅
		member.setMpassword(passwordEncoder.encode(member.getMpassword()));
		// 3. Member DTO의 menabled는 회원가입 시 사용자가 입력하는 값이 아님 -> Controller에서 따로 세팅
		// 회원가입된 직후에는 ID를 활성화시켜둔 상태여야하므로 ture로 세팅
		member.setMenabled(true);
		// 4. 회원가입을 통해 가입한 모든 사용자는 ROLE_USER 권한을 가지도록 Member DTO의 mrole을 ROLE_USER로 세팅
		member.setMrole("ROLE_USER");
		// 5. 회원가입 시 베리 1개를 제공 -> Member DTO의 mberry를 1로 세팅
		member.setMberry(1);

		// 값 설정이 모두 끝났으므로 memberService의 join() 호출하여 회원가입 처리
		// 매개변수로 Member DTO를 제공
		memberService.join(member);

		// 사용자에게 JSON 응답을 반환할 때 보안상의 이유로 비밀번호는 직접 노출되면 안됨
		// 따라서 JSON 응답에서는 비밀번호가 보이지 않도록 null로 값을 바꿔줌
		member.setMpassword(null);

		// JSON 응답을 반환
		return member;

	}
	
	// 관리자 페이지 - 전체 회원 조회 및 페이지네이션
	@GetMapping("/getAdminMemberList")
	public Map<String, Object> getAdminMemberList(@RequestParam(defaultValue = "1") int pageNo, 
			@RequestParam(defaultValue = "") String searchType, @RequestParam(defaultValue = "") String word) {
		log.info("searchType : " + searchType);
		log.info("word : " + word);
		
		int totalRows = 0;
		
		if (!word.equals("")) {
			totalRows = memberService.getMemberCountByWord(searchType, word);
		} else {			
			totalRows = memberService.getMemberCount();
		}

		Pager pager = new Pager(20, 5, totalRows, pageNo);
		log.info("pageNo" + pageNo);
		List<Member> list = memberService.getAdminMemberList(pager, searchType, word);
		Map<String, Object> map = new HashMap<>();
		map.put("member", list);
		map.put("pager", pager);

		return map;
	}

	@PostMapping("/idDuplicationCheck/{mid}")
	public Map<String, String> idDuplicationCheck(@PathVariable String mid) {

		log.info("mid : " + mid);
		
		int count = memberService.countByMid(mid);

		Map<String, String> map = new HashMap<>();

		log.info("count : " + count);
		
		if (count == 1) {
			map.put("result", "fail");
		} else {
			map.put("result", "success");
		}

		return map;
	}

	@PostMapping("/nicknameDuplicationCheck/{mnickname}")
	public Map<String, String> nicknameDuplicationCheck(@PathVariable String mnickname) {

		log.info("mnickname : " + mnickname);
		int count = memberService.countByMnickname(mnickname);
		log.info("count : " + count);
		Map<String, String> map = new HashMap<>();

		if (count == 1) {
			map.put("result", "fail");
		} else {
			map.put("result", "success");
		}

		return map;
	}

	@PostMapping("/phoneDuplicationCheck/{mphone}")
	public Map<String, String> phoneDuplicationCheck(@PathVariable String mphone) {
		log.info("mphone : " + mphone);
		int count = memberService.countByMphone(mphone);
		log.info("count : " + count);
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
		log.info("mpassword : " + mpassword);
		
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

	@PostMapping("/findId/{mphone}")
	public Map<String, Object> findId(@PathVariable String mphone) {
		log.info("mphone :" + mphone);
		// 아이디 찾기 시 입력한 휴대폰 번호가 DB에 있는지 확인
		String mid = memberService.findId(mphone);

		Map<String, Object> map = new HashMap<>();

		if (mid != null) {
			map.put("result", "success");
			map.put("mid", mid);
		} else {
			map.put("result", "fail");
		}

		return map;
	}

	@PostMapping("/findPassword/{mphone}/{mid}")
	public Map<String, Object> findPassword(@PathVariable String mphone, @PathVariable String mid) {
		log.info("mphone : " + mphone);
		log.info("mid : " + mid);
		
		// 아이디 찾기 시 입력한 휴대폰 번호가 DB에 있는지 확인
		int count = memberService.findPassword(mphone, mid);

		Map<String, Object> map = new HashMap<>();

		if (count == 1) {
			map.put("result", "success");
		} else {
			map.put("result", "fail");
		}

		return map;
	}
	
	@PutMapping("/resetMpassword/{mid}/{mpassword}")
	public void resetMpassword(@PathVariable String mid, @PathVariable String mpassword) {
		log.info("mid : " + mid);
		log.info("mpassword : " + mpassword);
		// 주입받은 letffleUserDetailsService를 이용해 loadUserByUsername를 호출
		// 매개변수로 입력받은 mid를 전달
		// DB에 해당 mid를 가진 유저의 데이터를 LetffleUserDetails 객체의 Member 필드에 저장
		// getMember()로 데이터에 접근이 가능해짐
		LetffleUserDetails userDetails = letffleUserDetailsService.loadUserByUsername(mid);

		// 변경할 새 비밀번호를 암호화 -> PasswordEncoder의 encode() 사용
		// 암호화한 비밀번호를 Member DTO의 mpassword 필드값으로 세팅
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		userDetails.getMember().setMpassword(passwordEncoder.encode(mpassword));

		// 유저의 아이디와 새 비밀번호를 DB에 저장하도록 memberService의 updateMpassword() 호출
		// 매개변수로 그 값들을 넘겨줌
		memberService.updateMpassword(userDetails.getMember().getMid(), userDetails.getMember().getMpassword());
	}
	
	// 마이페이지 내가쓴 게시물
	@GetMapping("/myBoardList")
	public Map<String, Object> getMyBoardList(Authentication authentication,
			@RequestParam(defaultValue = "1") int pageNo) {
		// 아이디에 부합한 값이 나타나도록 그값에 일치하는 것들을 가져오기위해서 authentication.getName 사용하였다.
		int totalRows = memberService.getMyBoardCount(authentication.getName());
		// 페이저 사용을 위해서 페이저의 행과 열에 맞춰서 나타냄
		Pager pager = new Pager(5, 5, totalRows, pageNo);

		// board에서 작성한 나의 리스트들을 가져오기 위해서 작성한 것이다.
		List<Board> list = memberService.getMyBoardList(pager, authentication.getName());

		Map<String, Object> map = new HashMap<>();

		map.put("board", list);
		map.put("pager", pager);
		return map;
	}

	// 마이페이지 -> 좋아요 목록 조회 메소드
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
	
	//래플 좋아요 상태 판정 기능
	@GetMapping("/likeStatus/{rno}")
	public boolean likeStatus(Authentication authentication, @PathVariable int rno) {
		return memberService.getLikeStatus(authentication.getName(), rno);
	}
	
	// 래플 좋아요 추가 기능
	@PostMapping("/addLikeList/{rno}")
	public void addLikeList(Authentication authentication, @PathVariable  int rno) {
		memberService.insertAddLikeList(authentication.getName(), rno);
	}

	// 래플 좋아료 삭제 기능
	@DeleteMapping("/deleteLikeList/{rno}")
	public String deleteLikeList(Authentication authentication,@PathVariable int rno) {
		memberService.deleteLikeList(authentication.getName(), rno);
		return "관심 래플 삭제";
	}

	// 마이페이지 -> 내가 작성한 댓글 목록 조회
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

	// 마이페이지 -> 비밀번호 수정
	@PutMapping("/mypage/updateMpassword/{mpassword}")
	public void updateMpassword(Authentication authentication, @PathVariable String mpassword) {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

		log.info("mpassword : " + mpassword);
		
		memberService.updateMpassword(authentication.getName(), passwordEncoder.encode(mpassword));
	}
	
	@PutMapping("/mypage/updateMnickname/{mnickname}")
	public void updateMnickname(@PathVariable String mnickname, Authentication authentication) {
		log.info("mnickname" + mnickname);
		
		memberService.updateMnickname(authentication.getName(), mnickname);
		
	}

	// 휴대폰 번호 수정
	@PutMapping("/mypage/updateMphone/{mphone}")
	public void updateMphone(@PathVariable String mphone, Authentication authentication) {
		memberService.updateMphone(authentication.getName(), mphone);
	}

	// 주소 수정
	@PutMapping("/mypage/updateMaddress/{mzipcode}/{maddress}")
	public void updateMaddress(@PathVariable String mzipcode, @PathVariable String maddress, Authentication authentication) {
		memberService.updateMaddress(authentication.getName(), mzipcode, maddress);
	}

	// 회원 탈퇴
	// DB에서 데이터 삭제가 아닌 menabled를 false로 바꾸는 것으로 탈퇴 처리
	// 따라서 delete가 아닌 put을 사용
	@PutMapping("/deleteMember")
	public void deleteMember(Authentication authentication) {
		// 해당 mid의 menabled 값을 false로 수정하는 memberService의 deleteByMid() 호출
		// 매개변수로 mid를 넘겨줌
		memberService.deleteByMid(authentication.getName());
	}

	// 문의 등록
	@PostMapping("/mypage/createInquiry")
	public Inquiry createInquiry(Inquiry inquiry, Authentication authentication) {
		// 첨부파일이 들어있는지 확인
		
		inquiry.setMid(authentication.getName());
		
		if (inquiry.getIattach() != null && !inquiry.getIattach().isEmpty()) {
			// 첨부파일이 포함된 경우
			// MultipartFile 객체에 첨부파일 데이터를 저장
			MultipartFile mf = inquiry.getIattach();
			// Inquiry 객체의 iattachoname, Iattachtype 필드 값으로 각각 파일명과 확장자명을 저장
			inquiry.setIattachoname(mf.getOriginalFilename());
			inquiry.setIattachtype(mf.getContentType());
			// Inquiry 객체의 iattachdata 필드에 바이너리 데이터를 저장
			try {
				inquiry.setIattachdata(mf.getBytes());
			} catch (IOException e) {
			}
		}

		// Inquiry를 저장하도록 memberService의 insertInquiry() 호출
		// 매개변수로 Inquiry 객체를 넘겨줌
		memberService.insertInquiry(inquiry);

		// JSON 응답에는 바이너리 데이터가 출력되지 않음
		// 따라서 해당 필드값을 null로 변경 후 응답을 반환
		inquiry.setIattach(null);
		inquiry.setIattachdata(null);

		return inquiry;
	}

	// 문의 목록 가져오기 (추후 권한을 매개변수로 받아서 조건에 따라 다른 값을 리턴할 예정)
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
	@GetMapping("/getUserInquiryList")
	public Map<String, Object> getUserInquiryList(@RequestParam(defaultValue = "1") int pageNo, Authentication authentication) {
		int totalRows = memberService.getUserInquiryCount(authentication.getName(), authentication.getAuthorities().iterator().next().toString());
		Pager pager = new Pager(5, 5, totalRows, pageNo);
		// 로그인한 유저의 이름과 유저의 권한을 얻어오기
		List<Inquiry> list = memberService.getUserInquiryList(pager, authentication.getName(), authentication.getAuthorities().iterator().next().toString());
		Map<String, Object> map = new HashMap<>();
		map.put("inquiry", list);
		map.put("pager", pager);
		return map;
	}
	
	
	
	// 문의 상세 보기
	@GetMapping("/inquiryDetail/{ino}")
	// @pathvarialbe을 사용하는 이유 매개변수를 바인딩 시키기 위해서 사용된것이다.
	public Inquiry inquiryDetail(@PathVariable int ino) {
		log.info("ino:" + ino);
		// 전달받은 ino와 일치하는 Inquiry 객체를 DB에서 가져오기
		Inquiry inquiry = memberService.readInquiry(ino);

		// 파일명을 보내는 클라이언트에게 파일의 값을 안보여주기 위해서 null로 처리를 한것이다.
		inquiry.setIattachdata(null);

		return inquiry;
	}

	// 문의 수정
	@PutMapping("/updateInquiry")
	public Inquiry updateInquiry(Inquiry inquiry) {
		// 첨부파일이 포함되어 있는지 검사
		if (inquiry.getIattach() != null && !inquiry.getIattach().isEmpty()) {
			// 첨부파일이 포함된 경우
			// MultipartFile 객체에 첨부파일 데이터를 저장
			MultipartFile mf = inquiry.getIattach();

			// Inquiry 객체의 iattachoname, Iattachtype 필드 값으로 각각 파일명과 확장자명을 저장
			inquiry.setIattachoname(mf.getOriginalFilename());
			inquiry.setIattachtype(mf.getContentType());

			// Inquiry 객체의 iattachdata 필드에 바이너리 데이터를 저장
			try {
				inquiry.setIattachdata(mf.getBytes());
			} catch (IOException e) {
			}
		}

		// 변경된 값으로 Inquiry를 저장하도록 memberService의 updateInquiry() 호출
		// 매개변수로 Inquiry 객체를 넘겨줌
		memberService.updateInquiry(inquiry);

		// JSON 응답에는 바이너리 데이터가 출력되지 않음
		// 따라서 해당 필드값을 null로 변경 후 응답을 반환
		inquiry.setIattach(null);
		inquiry.setIattachdata(null);

		return inquiry;
	}
	
	// 문의 첨부 다운로드용
	@GetMapping("/iattach/{ino}")
	public void iattach(@PathVariable int ino, HttpServletResponse response) {
		// 해당 Board 객체 가져오기
		Inquiry inquiry = memberService.selectInquiryIno(ino);
		log.info("inquiry : " + inquiry);
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
			log.info("" + ino);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	@GetMapping("/getMyPageDashboard")
	public Map<String, Object> getMyPageDashboard(Authentication authentication){
		return memberService.getMyPageDashboard(authentication.getName());
	}

	// BerryHistory 관련 메소드
	// 베리 사용내역 생성 메소드

	// 베리 사용내역 조회 메소드
	@GetMapping("/getBerryHistoryList")
	public Map<String, Object> getBerryHistoryList(Authentication authentication, @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "Total")String option) {
		return memberService.getBerryHistoryList(authentication.getName(), pageNo, option);
	}
	
	@GetMapping("/getBerryHistoryListForHome")
	public List<BerryHistory> getBerryHistoryListForHome(Authentication authentication) {
		log.info("실행");
		List<BerryHistory> list = memberService.getBerryHistoryUpToTen(authentication.getName());
		return list;
	}

	// 문의 내용 답변 작성
	@PutMapping("/inquiryReply")
	public void inquiryReply(@RequestParam int ino, @RequestParam String ireply) {
		// 회원에게 작성된 문의 답변해주기 위해서 ino, ireply를 사용
		memberService.updateInquiryReply(ino, ireply);
	}
	
	@PutMapping("/updateWinner")
	public void updateWinner(Winner winner, Authentication authentication) {
		winner.setMid(authentication.getName());
		memberService.updateWinner(winner);
	}
	
	@GetMapping("/getMyAddress")
	public Member getMyAddress(Authentication authentication) {
		return memberService.selectByMid(authentication.getName());
	}

	// 관리자 페이지 당첨자 조회
	@GetMapping("/getAdminWinnerList")
	public Map<String, Object> getWinnerList(@RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "") String searchType, @RequestParam(defaultValue = "") String word) {
		
		log.info("searchType : " + searchType);
		log.info("word : " + word);
		
		// 당첨자의 수를 얻어옴 
		int totalRows = 0;
		
		if (!word.equals("")) {
			totalRows = memberService.getWinnerCountByWord(searchType, word);
		} else {			
			totalRows = memberService.getWinnerCount();
		}
		log.info("totalRows : " + totalRows);

		Pager pager = new Pager(30, 5, totalRows, pageNo);
		log.info("pageNo : " + pageNo);
		List<RaffleDetailRequest> list = memberService.getAdminWinnerList(pager, searchType, word);
		log.info("list : " + list.toString());
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
	@GetMapping("/getMemberDetail/{mid}")
	public Member getMemberDetail(@PathVariable String mid) {
		Member member = memberService.getMember(mid);
		return member;
	}
	
}

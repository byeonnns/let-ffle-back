package com.mycompany.let_ffle.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.let_ffle.dto.Pager;
import com.mycompany.let_ffle.dto.Raffle;
import com.mycompany.let_ffle.dto.RaffleDetail;
import com.mycompany.let_ffle.dto.TimeMission;
import com.mycompany.let_ffle.dto.Winner;
import com.mycompany.let_ffle.dto.request.RaffleDetailRequest;
import com.mycompany.let_ffle.dto.request.RaffleRequest;
import com.mycompany.let_ffle.service.MemberService;
import com.mycompany.let_ffle.service.RaffleService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/raffle")

public class RaffleController {

	@Autowired
	private RaffleService raffleService;
	@Autowired
	private MemberService memberService;

	@PostMapping("/createRaffle")
	public RaffleRequest createRaffle(RaffleRequest raffleRequest) {

		// (임시) - 나중에 db로 받아올 것 Timestamp가 postman형식으로 넘어가지 못해 객체를 생성해 TimeMission(dto),
		// Raffle(dto)의 Timestamp 를 설정
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		TimeMission timeMission = new TimeMission();
		timeMission.setTstartedat(timestamp);
		timeMission.setTfinishedat(timestamp);
		raffleRequest.setTimeMission(timeMission);
		raffleRequest.getRaffle().setRstartedat(timestamp);
		raffleRequest.getRaffle().setRfinishedat(timestamp);

		// raffleRequest(dto)의 RaffleImage(dto) 안에 Rthumbnailattach 가 null값이 아니고 비어있지
		// 않으면 파일 이름과, 종류, 데이터를 설정
		if (raffleRequest.getRaffleImage().getRthumbnailattach() != null
				&& !raffleRequest.getRaffleImage().getRthumbnailattach().isEmpty()) {
			MultipartFile mf = raffleRequest.getRaffleImage().getRthumbnailattach();
			// 파일 이름을 설정
			raffleRequest.getRaffleImage().setRthumbnailimgoname(mf.getOriginalFilename());
			// 파일 종류를 설정
			raffleRequest.getRaffleImage().setRthumbnailimgtype(mf.getContentType());
			try {
				// 파일 데이터를 설정0
				raffleRequest.getRaffleImage().setRthumbnailimg(mf.getBytes());
			} catch (IOException e) {
			}
		}

		// raffleRequest(dto)의 RaffleImage(dto) 안에 Rgiftattach가 null 값이 아니고 비어있지 않으면 파일
		// 이름과, 종류, 데이터를 설정
		if (raffleRequest.getRaffleImage().getRgiftattach() != null
				&& !raffleRequest.getRaffleImage().getRgiftattach().isEmpty()) {
			MultipartFile mf = raffleRequest.getRaffleImage().getRgiftattach();
			// 파일 이름 설정
			raffleRequest.getRaffleImage().setRgiftimgoname(mf.getOriginalFilename());
			// 파일 종류 설정
			raffleRequest.getRaffleImage().setRgiftimgtype(mf.getContentType());
			try {
				// 파일 데이터를 설정
				raffleRequest.getRaffleImage().setRgiftimg(mf.getBytes());
			} catch (IOException e) {
			}

		}

		// raffleRequest(dto)의 RaffleImage(dto) 안에 Rdetailattach가 null 값이 아니고 비어있지 않으면
		// 파일 이름과, 종류, 데이터를 설정
		if (raffleRequest.getRaffleImage().getRdetailattach() != null
				&& !raffleRequest.getRaffleImage().getRdetailattach().isEmpty()) {
			MultipartFile mf = raffleRequest.getRaffleImage().getRdetailattach();
			// 파일 이름 설정
			raffleRequest.getRaffleImage().setRdetailimgoname(mf.getOriginalFilename());
			// 파일 종류 설정
			raffleRequest.getRaffleImage().setRdetailimgtype(mf.getContentType());

			try {
				// 파일 데이터를 설정
				raffleRequest.getRaffleImage().setRdetailimg(mf.getBytes());
			} catch (IOException e) {

			}
		}
		// json으로 변환되지 않는 필드를 null 처리하기 위함
		raffleService.insertRaffle(raffleRequest);
		raffleRequest.getRaffleImage().setRthumbnailattach(null);
		raffleRequest.getRaffleImage().setRthumbnailimg(null);
		raffleRequest.getRaffleImage().setRdetailattach(null);
		raffleRequest.getRaffleImage().setRdetailimg(null);
		raffleRequest.getRaffleImage().setRgiftattach(null);
		raffleRequest.getRaffleImage().setRgiftimg(null);
		return raffleRequest;
	}

	@GetMapping("/getAdminRaffleList")
	public Map<String, Object> getAdminRaffleList(@RequestParam(defaultValue = "1") int pageNo) {
		int totalRows = raffleService.getCount();

		Pager pager = new Pager(5, 5, totalRows, pageNo);

		List<Raffle> list = raffleService.getListForAdmin(pager);
		Map<String, Object> map = new HashMap<>();

		map.put("Raffle", list);
		map.put("pager", pager);

		return map;
	}

	@GetMapping("/getRaffleList")
	public List<RaffleRequest> getRaffleList() {
		List<RaffleRequest> list = raffleService.getListForUser();
		// 포스트맨에서 이미지의 데이터이름 너무길기때문에 향상된 포문으로 rr:list 이런 형식으로 작성한이유는 리스트가 끝날때까지 모든요소에
		// 레플이미지 객체를 널처리해주기 위해 사용(rr는 변수이름)
		for (RaffleRequest rr : list) {
			rr.setRaffleImage(null);
		}
		return list;
	}

	@GetMapping("/readRaffle/{rno}")
	public RaffleRequest readRaffle(@PathVariable int rno) {
		RaffleRequest raffleRequest = raffleService.readRaffle(rno);
		return raffleRequest;
	}

	@PutMapping("/updateRaffle")
	public RaffleRequest updateRaffle(RaffleRequest raffleRequest) {
		return null;
	}

	@PutMapping("/deleteRaffle")
	public Raffle deleteRaffle(int rno) {
		return null;
	}

	@PostMapping("/createRaffleDetail")
	public RaffleDetail createRaffleDetail(RaffleDetail raffleDetail) {
		// raffleDetail(dto)를 매개변수로 받아 raffleService 메소드를 호출해 raffleDetail객체를 데이터 베이스에
		// 저장하도록 처리
		raffleService.insertRaffleDetail(raffleDetail);
		return null;
	}

	@GetMapping("/readRaffleDetail")
	public RaffleDetail raffleDetail(RaffleDetail raffleDetail) {
		// raffleDetail(dto)를 매개변수로 raffleService 메소드를 호출해 데이터베이스에 저장된 값을 raffleDetail
		// 객체로 받아 리턴해줌
		raffleDetail = raffleService.readRaffleDetail(raffleDetail);
		return raffleDetail;
	}

	// 마이페이지 -> 응모내역 조회 메소드
	@GetMapping("/getRaffleDetailList")
	public Map<String, Object> getRaffleDetailList(Authentication authentication) {
		Map<String, Object> map = new HashMap<>();
		map = raffleService.getRaffleDetailList(authentication.getName(), authentication.getAuthorities().iterator().next().toString());
		return map;
	}
	
	// 마이페이지 -> 내가 응모한 내역 기간별 조회
	@GetMapping("/getMyRaffleDetailRequestList")
	public List<RaffleDetail> getMyRaffleDetailRequestList(Authentication authentication, String startdate, String enddate) {
		List<RaffleDetail> list = raffleService.getMyRaffleDetailRequestList(authentication.getName(),startdate, enddate);
		return list;
		
	}

	@PostMapping("/createWinner")
	public Winner createWinner(Winner winner) {
		// winner(dto)를 매개변수로 받아 raffleService 메소드를 호출해 winner객체를 데이터베이스에 저장하도록 처리
		raffleService.insertWinner(winner);
		return null;
	}

	@GetMapping("/readWinnerDetail")
	public Winner readWinnerDetail(int rno) {
		Winner winner = raffleService.readWinnerDetail(rno);
		return winner;
	}

	// 마이페이지 -> 당첨내역 조회 메소드
	@GetMapping("/getWinnerDetailList")
	// 매개변수로 authentication 받음
	public List<Raffle> getWinnerDetailList(Authentication authentication) {
		List<Raffle> list = raffleService.getWinnerDetailList(authentication.getName());
		return list;
	}
		

	// 미션 참여 여부 수정
	@PutMapping("/updateRdtMissionCleared")
	// 매개변수로 rno, authentication을 통해 회원의 이름을 가져오기 위함, manswer(회원이 제출할 퀴즈정답)
	public void updateRdtMissionCleared(int rno, Authentication authentication, String manswer) {
		// raffleService로 매개변수로를 넘겨 로직 처리를 요청함
		raffleService.updateRdtMissionCleared(rno, authentication.getName(), manswer);

	}

	// 베리 사용내역
	@PutMapping("/updateRdtBerrySpend")
	public String updateRdtBerrySpend(int rno, Authentication authentication, int RdtBerrySpend) {
		String result;
		if(RdtBerrySpend>0 && RdtBerrySpend<=10)
			result = raffleService.updateRdtBerrySpend(rno, authentication.getName(), RdtBerrySpend);
		else{
			result = "늘어난줄 알았지? 응 아니야.";
		}
		
		return result;
	}
}

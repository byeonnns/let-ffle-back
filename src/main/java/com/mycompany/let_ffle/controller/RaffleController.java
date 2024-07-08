package com.mycompany.let_ffle.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
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
import com.mycompany.let_ffle.dto.RaffleImage;
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

	@Transactional
	@PostMapping("/createRaffle")
	public RaffleRequest createRaffle(RaffleRequest raffleRequest) {
		log.info("실행");
		log.info("raffleRequest : " + raffleRequest);

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
	public List<RaffleRequest> getRaffleList(@RequestParam(defaultValue = "all") String category, @RequestParam(defaultValue = "popular") String sortType) {
		List<RaffleRequest> list = raffleService.getListForUser(category, sortType);
		// 포스트맨에서 이미지의 데이터이름 너무길기때문에 향상된 포문으로 rr:list 이런 형식으로 작성한이유는 리스트가 끝날때까지 모든요소에
		// 레플이미지 객체를 널처리해주기 위해 사용(rr는 변수이름)
		log.info("rcategory : " + category);
		log.info("sortType : " + sortType);
		for (RaffleRequest rr : list) {
			rr.setRaffleImage(null);
		}
		return list;
	}
	
	@GetMapping("/getNewReleaseRaffles")
	public List<RaffleRequest> getNewReleaseRaffles() {
		List<RaffleRequest> list = raffleService.getNewReleaseRaffles();
		// 포스트맨에서 이미지의 데이터이름 너무길기때문에 향상된 포문으로 rr:list 이런 형식으로 작성한이유는 리스트가 끝날때까지 모든요소에
		// 레플이미지 객체를 널처리해주기 위해 사용(rr는 변수이름)
		for (RaffleRequest rr : list) {
			rr.setRaffleImage(null);
		}
		return list;
	}
	
	@GetMapping("/getCutOffSoonRaffles")
	public List<RaffleRequest> getCutOffSoonRaffles() {
		List<RaffleRequest> list = raffleService.getCutOffSoonRaffles();
		// 포스트맨에서 이미지의 데이터이름 너무길기때문에 향상된 포문으로 rr:list 이런 형식으로 작성한이유는 리스트가 끝날때까지 모든요소에
		// 레플이미지 객체를 널처리해주기 위해 사용(rr는 변수이름)
		for (RaffleRequest rr : list) {
			rr.setRaffleImage(null);
		}
		return list;
	}
	
	@GetMapping("/searchRaffleList/{word}")
	public List<RaffleRequest> searchRaffleList(@PathVariable String word) {
		log.info("word : " + word);
		List<RaffleRequest> list = raffleService.searchRaffle(word);
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
		raffleService.updateRaffle(raffleRequest);

		return raffleRequest;
	}

	@PutMapping("/deleteRaffle")
	public Raffle deleteRaffle(int rno) {
		raffleService.deleteRaffle(rno);
		return null;
	}

	@PostMapping("/createRaffleDetail/{rno}")
	public RaffleDetail createRaffleDetail(@PathVariable int rno, Authentication authentication) {
		RaffleDetail raffleDetail = new RaffleDetail();
		
		raffleDetail.setRno(rno);
		raffleDetail.setMid(authentication.getName());
		
		raffleService.insertRaffleDetail(raffleDetail);
		
		return raffleDetail;
	}

	//내가 래플에 응모했는지에 대한 여부 판정
	@GetMapping("/readRaffleDetail/{rno}")
	public Map <String, Object> raffleDetail(Authentication authentication, @PathVariable int rno) {
		// raffleDetail(dto)를 매개변수로 raffleService 메소드를 호출해 데이터베이스에 저장된 값을 raffleDetail
		Map <String, Object> map = new HashMap<>();
		map.put("raffleDetail", raffleService.readRaffleDetail(authentication.getName(), rno));
		map.put("raffleStatus", raffleService.readRaffleDetailStatus(authentication.getName(), rno));
		return map;
	}

	// 마이페이지 -> 응모내역 조회 메소드
	@GetMapping("/getRaffleDetailList")
	public Map<String, Object> getRaffleDetailList(Authentication authentication, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "Total") String status, @RequestParam(defaultValue = "null")String start, @RequestParam(defaultValue = "null")String end) {
		Map<String, Object> map = new HashMap<>();
		map = raffleService.getRaffleDetailList(authentication.getName(),
				authentication.getAuthorities().iterator().next().toString(), pageNo, status, start, end);

		return map;
	}


	// 마이페이지 -> 내가 응모한 내역 기간별 조회
	@GetMapping("/getMyRaffleDetailRequestList")
	public List<RaffleDetail> getMyRaffleDetailRequestList(Authentication authentication, String startdate,
			String enddate) {
		List<RaffleDetail> list = raffleService.getMyRaffleDetailRequestList(authentication.getName(), startdate,
				enddate);
		return list;

	}


	@PostMapping("/createWinner")
	public String createWinner(@RequestParam int rno, Authentication authentication) {
		// winner(dto)를 매개변수로 받아 raffleService 메소드를 호출해 winner객체를 데이터베이스에 저장하도록 처리
		return raffleService.insertWinner(rno, authentication.getName());
	}

	@GetMapping("/readWinnerDetail")
	public Winner readWinnerDetail(int rno) {
		Winner winner = raffleService.readWinnerDetail(rno);
		return winner;
	}

	// 마이페이지 -> 당첨내역 조회 메소드
	@GetMapping("/getWinnerDetailList")
	// 매개변수로 authentication 받음
	public Map<String, Object>getWinnerDetailList(Authentication authentication, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "null")String start, @RequestParam(defaultValue = "null")String end) {
		Map<String, Object> map = new HashMap<>();
		int totalRows = raffleService.getWinRaffleCount(authentication.getName(), start, end);
		Pager pager = new Pager(5, 5, totalRows, pageNo);
		List<Raffle> list = raffleService.getWinnerDetailList(authentication.getName(), pager, start, end);
		map.put("list", list);
		map.put("pager", pager);
		return map;
	}

	// 미션 참여 여부 수정
	@PutMapping("/updateRdtMissionCleared/{rno}/{manswer}")
	// 매개변수로 rno, authentication을 통해 회원의 이름을 가져오기 위함, manswer(회원이 제출할 퀴즈정답)
	public void updateRdtMissionCleared(@PathVariable int rno, Authentication authentication, @PathVariable String manswer) {
		// raffleService로 매개변수로를 넘겨 로직 처리를 요청함
		raffleService.updateRdtMissionCleared(rno, authentication.getName(), manswer);
	}
	
	// 베리 사용내역
	@PutMapping("/updateRdtBerrySpend")
	public String updateRdtBerrySpend(int rno, int rdtBerrySpend, Authentication authentication) {
		log.info("사용한 베리");
		String result;
		if (rdtBerrySpend > 0 && rdtBerrySpend <= 10)
			result = raffleService.updateRdtBerrySpend(rno, authentication.getName(), rdtBerrySpend);
		else {
			result = "늘어난줄 알았지? 응 아니야.";
		}

		return result;
	}

	// 썸네일 이미지
	@GetMapping("/raffleThumbnailAttach/{rno}")
	public void download(@PathVariable int rno, HttpServletResponse response) {
		// 해당 게시물 가져오기
		RaffleImage raffleImage = raffleService.readRaffleImage(rno);
		// 파일 이름이 한글일 경우, 브라우저에서 한글 이름으로 다운로드 받기 위한 코드
		try {
			String fileName = new String(raffleImage.getRthumbnailimgoname().getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			// 파일 타입을 헤더에 추가
			response.setContentType(raffleImage.getRthumbnailimgtype());
			// 응답 바디에 파일 데이터를 출력
			OutputStream os = response.getOutputStream();
			os.write(raffleImage.getRthumbnailimg());
			os.flush();
			os.close();
		} catch (IOException e) {
			log.error(e.toString());
		}
	}

	// 경품 이미지
	@GetMapping("/raffleGiftAttach/{rno}")
	public void downloadGift(@PathVariable int rno, HttpServletResponse response) {
		// 해당 게시물 가져오기
		RaffleImage raffleImage = raffleService.readRaffleImage(rno);
		// 파일 이름이 한글일 경우, 브라우저에서 한글 이름으로 다운로드 받기 위한 코드
		try {
			String fileName = new String(raffleImage.getRgiftimgoname().getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			// 파일 타입을 헤더에 추가
			response.setContentType(raffleImage.getRgiftimgtype());
			// 응답 바디에 파일 데이터를 출력
			OutputStream os = response.getOutputStream();
			os.write(raffleImage.getRgiftimg());
			os.flush();
			os.close();
		} catch (IOException e) {
			log.error(e.toString());
		}
	}

	// 레플 디테일 이미지
	@GetMapping("/raffleDetailAttach/{rno}")
	public void downloadDetail(@PathVariable int rno, HttpServletResponse response) {
		// 해당 게시물 가져오기
		RaffleImage raffleImage = raffleService.readRaffleImage(rno);
		// 파일 이름이 한글일 경우, 브라우저에서 한글 이름으로 다운로드 받기 위한 코드
		try {
			String fileName = new String(raffleImage.getRdetailimgoname().getBytes("UTF-8"), "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			// 파일 타입을 헤더에 추가
			response.setContentType(raffleImage.getRdetailimgtype());
			// 응답 바디에 파일 데이터를 출력
			OutputStream os = response.getOutputStream();
			os.write(raffleImage.getRdetailimg());
			os.flush();
			os.close();
		} catch (IOException e) {
			log.error(e.toString());
		}
	}
	
	// 관리자 페이지 전체 회원 - ( 래플 참여 현황 ) 
	@GetMapping("/getAdminRaffleDetail/{mid}")
	public List<RaffleDetailRequest> getAdminRaffleDetail(@PathVariable String mid) {
		List<RaffleDetailRequest> list = raffleService.getAdminRaffleDetail(mid);
		log.info(mid);
		return list;
	}
}

package com.mycompany.let_ffle.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.mycompany.let_ffle.service.RaffleService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/raffle")

public class RaffleController {

	@Autowired
	private RaffleService raffleService;

	// 래플 등록
	@Transactional
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PostMapping("/createRaffle")
	public RaffleRequest createRaffle(RaffleRequest raffleRequest) {
		// 첨부 파일 포함 여부 검사
		if (raffleRequest.getRaffleImage().getRthumbnailattach() != null
				&& !raffleRequest.getRaffleImage().getRthumbnailattach().isEmpty()) {
			MultipartFile mf = raffleRequest.getRaffleImage().getRthumbnailattach();
			raffleRequest.getRaffleImage().setRthumbnailimgoname(mf.getOriginalFilename());
			raffleRequest.getRaffleImage().setRthumbnailimgtype(mf.getContentType());
			try {
				raffleRequest.getRaffleImage().setRthumbnailimg(mf.getBytes());
			} catch (IOException e) {
			}
		}

		if (raffleRequest.getRaffleImage().getRgiftattach() != null
				&& !raffleRequest.getRaffleImage().getRgiftattach().isEmpty()) {
			MultipartFile mf = raffleRequest.getRaffleImage().getRgiftattach();
			raffleRequest.getRaffleImage().setRgiftimgoname(mf.getOriginalFilename());
			raffleRequest.getRaffleImage().setRgiftimgtype(mf.getContentType());
			try {
				raffleRequest.getRaffleImage().setRgiftimg(mf.getBytes());
			} catch (IOException e) {
			}
		}

		if (raffleRequest.getRaffleImage().getRdetailattach() != null
				&& !raffleRequest.getRaffleImage().getRdetailattach().isEmpty()) {
			MultipartFile mf = raffleRequest.getRaffleImage().getRdetailattach();
			raffleRequest.getRaffleImage().setRdetailimgoname(mf.getOriginalFilename());
			raffleRequest.getRaffleImage().setRdetailimgtype(mf.getContentType());
			try {
				raffleRequest.getRaffleImage().setRdetailimg(mf.getBytes());
			} catch (IOException e) {
			}
		}

		raffleService.insertRaffle(raffleRequest);

		// JSON 응답 생성 : 변환되지 않는 필드를 null 처리
		raffleRequest.getRaffleImage().setRthumbnailattach(null);
		raffleRequest.getRaffleImage().setRthumbnailimg(null);
		raffleRequest.getRaffleImage().setRdetailattach(null);
		raffleRequest.getRaffleImage().setRdetailimg(null);
		raffleRequest.getRaffleImage().setRgiftattach(null);
		raffleRequest.getRaffleImage().setRgiftimg(null);

		return raffleRequest;
	}

	// 관리자용 래플 목록 가져오기
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/getAdminRaffleList")
	public Map<String, Object> getAdminRaffleList(@RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "") String word) {
		int totalRows = 0;

		if (!word.equals("")) {
			totalRows = raffleService.getRaffleCountByWord(word);
		} else {
			totalRows = raffleService.getRaffleCount();
		}

		Pager pager = new Pager(5, 5, totalRows, pageNo);

		List<Raffle> list = raffleService.getRaffleListForAdmin(pager, word);

		Map<String, Object> map = new HashMap<>();
		map.put("Raffle", list);
		map.put("pager", pager);

		return map;
	}

	// 사용자용 래플 목록 가져오기
	@GetMapping("/getRaffleList")
	public List<RaffleRequest> getRaffleList(@RequestParam(defaultValue = "all") String category,
			@RequestParam(defaultValue = "popular") String sortType) {
		List<RaffleRequest> list = raffleService.getRaffleListForUser(category, sortType);

		for (RaffleRequest rr : list) {
			rr.setRaffleImage(null);
		}

		return list;
	}

	// 메인 페이지용 최신 등록 래플 가져오기
	@GetMapping("/getNewReleaseRaffles")
	public List<RaffleRequest> getNewReleaseRaffles() {
		List<RaffleRequest> list = raffleService.getNewReleaseRaffles();

		for (RaffleRequest rr : list) {
			rr.setRaffleImage(null);
		}

		return list;
	}

	// 메인 페이지용 마감 임박 래플 가져오기
	@GetMapping("/getCutOffSoonRaffles")
	public List<RaffleRequest> getCutOffSoonRaffles() {
		List<RaffleRequest> list = raffleService.getCutOffSoonRaffles();

		for (RaffleRequest rr : list) {
			rr.setRaffleImage(null);
		}

		return list;
	}

	// 래플 목록 검색 기능
	@GetMapping("/searchRaffleList/{word}")
	public List<RaffleRequest> searchRaffleList(@PathVariable String word) {
		List<RaffleRequest> list = raffleService.searchRaffle(word);

		for (RaffleRequest rr : list) {
			rr.setRaffleImage(null);
		}

		return list;
	}

	// 래플 상세
	@GetMapping("/readRaffle/{rno}")
	public RaffleRequest readRaffle(@PathVariable int rno) {
		return raffleService.readRaffle(rno);
	}

	// 래플 수정
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PutMapping("/updateRaffle")
	public RaffleRequest updateRaffle(RaffleRequest raffleRequest) {
		if (raffleRequest.getRaffleImage() != null) {
			if (raffleRequest.getRaffleImage().getRthumbnailattach() != null
					&& !raffleRequest.getRaffleImage().getRthumbnailattach().isEmpty()) {
				MultipartFile mf = raffleRequest.getRaffleImage().getRthumbnailattach();
				raffleRequest.getRaffleImage().setRthumbnailimgoname(mf.getOriginalFilename());
				raffleRequest.getRaffleImage().setRthumbnailimgtype(mf.getContentType());
				try {
					raffleRequest.getRaffleImage().setRthumbnailimg(mf.getBytes());
				} catch (IOException e) {
				}
			}

			if (raffleRequest.getRaffleImage().getRgiftattach() != null
					&& !raffleRequest.getRaffleImage().getRgiftattach().isEmpty()) {
				MultipartFile mf = raffleRequest.getRaffleImage().getRgiftattach();
				raffleRequest.getRaffleImage().setRgiftimgoname(mf.getOriginalFilename());
				raffleRequest.getRaffleImage().setRgiftimgtype(mf.getContentType());
				try {
					raffleRequest.getRaffleImage().setRgiftimg(mf.getBytes());
				} catch (IOException e) {
				}
			}

			if (raffleRequest.getRaffleImage().getRdetailattach() != null
					&& !raffleRequest.getRaffleImage().getRdetailattach().isEmpty()) {
				MultipartFile mf = raffleRequest.getRaffleImage().getRdetailattach();
				raffleRequest.getRaffleImage().setRdetailimgoname(mf.getOriginalFilename());
				raffleRequest.getRaffleImage().setRdetailimgtype(mf.getContentType());
				try {
					raffleRequest.getRaffleImage().setRdetailimg(mf.getBytes());
				} catch (IOException e) {
				}
			}
		}

		raffleService.updateRaffle(raffleRequest);

		return raffleRequest;
	}

	// 래플 삭제
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PutMapping("/deleteRaffle")
	public void deleteRaffle(int rno) {
		raffleService.deleteRaffle(rno);
	}

	// 래플 응모 기능
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PostMapping("/createRaffleDetail/{rno}")
	public String createRaffleDetail(@PathVariable int rno, Authentication authentication) {
		RaffleDetail raffleDetail = new RaffleDetail();
		raffleDetail.setRno(rno);
		raffleDetail.setMid(authentication.getName());
		return raffleService.insertRaffleDetail(raffleDetail);
	}

	// 내가 래플에 응모했는지에 대한 여부 판정
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/readRaffleDetail/{rno}")
	public Map<String, Object> raffleDetail(Authentication authentication, @PathVariable int rno) {
		Map<String, Object> map = new HashMap<>();
		map.put("mberry", raffleService.countMyBerry(authentication.getName()));
		map.put("raffleDetail", raffleService.readRaffleDetail(authentication.getName(), rno));
		map.put("raffleStatus", raffleService.readRaffleDetailStatus(authentication.getName(), rno));

		return map;
	}

	// 응모 내역 조회
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/getRaffleDetailList")
	public Map<String, Object> getRaffleDetailList(Authentication authentication,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "Total") String status,
			@RequestParam(defaultValue = "null") String start, @RequestParam(defaultValue = "null") String end) {
		Map<String, Object> map = new HashMap<>();
		map = raffleService.getRaffleDetailList(authentication.getName(),
				authentication.getAuthorities().iterator().next().toString(), pageNo, status, start, end);

		return map;
	}

	// 응모 내역 기간별 조회
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/getMyRaffleDetailRequestList")
	public List<RaffleDetail> getMyRaffleDetailRequestList(Authentication authentication, String startdate,
			String enddate) {
		return raffleService.getMyRaffleDetailRequestList(authentication.getName(), startdate, enddate);
	}

	// 당첨자 등록
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PostMapping("/createWinner")
	public String createWinner(@RequestParam int rno, Authentication authentication) {
		return raffleService.insertWinner(rno, authentication.getName());
	}

	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/readWinnerDetail")
	public Map<String, Object> readWinnerDetail(@RequestParam int rno, @RequestParam(defaultValue = "1") int pageNo) {
		int totalRows = raffleService.getMonitorWinCount(rno);

		Pager pager = new Pager(5, 5, totalRows, pageNo);

		List<Winner> winner = raffleService.readWinnerDetail(rno, pager);

		Map<String, Object> map = new HashMap<>();
		map.put("winner", winner);
		map.put("pager", pager);

		return map;
	}

	// 당첨 목록 조회
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/getWinnerDetailList")
	public Map<String, Object> getWinnerDetailList(Authentication authentication,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "null") String start,
			@RequestParam(defaultValue = "null") String end) {
		int totalRows = raffleService.getWinRaffleCount(authentication.getName(), start, end);

		Pager pager = new Pager(5, 5, totalRows, pageNo);

		List<Raffle> list = raffleService.getWinnerDetailList(authentication.getName(), pager, start, end);

		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("pager", pager);

		return map;
	}

	// 미션 참여 여부 수정
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PutMapping("/updateRdtMissionCleared/{rno}/{manswer}")
	public String updateRdtMissionCleared(@PathVariable int rno, Authentication authentication,
			@PathVariable String manswer) {
		return raffleService.updateRdtMissionCleared(rno, authentication.getName(), manswer);
	}

	// 베리 사용 내역
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PutMapping("/updateRdtBerrySpend")
	public String updateRdtBerrySpend(int rno, int rdtBerrySpend, Authentication authentication) {
		
		log.info(rdtBerrySpend + "");
		
		String result = "";
		if (rdtBerrySpend > 0 && rdtBerrySpend <= 10) {			
			result = raffleService.updateRdtBerrySpend(rno, authentication.getName(), rdtBerrySpend);
		} else {
			result = "fail";
		}

		return result;
	}

	// 썸네일 이미지 요청
	@GetMapping("/raffleThumbnailAttach/{rno}")
	public void download(@PathVariable int rno, HttpServletResponse response) {
		RaffleImage raffleImage = raffleService.getThumbnailImage(rno);
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

	// 경품 이미지 요청
	@GetMapping("/raffleGiftAttach/{rno}")
	public void downloadGift(@PathVariable int rno, HttpServletResponse response) {
		RaffleImage raffleImage = raffleService.getGiftImage(rno);
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

	// 레플 디테일 이미지 요청
	@GetMapping("/raffleDetailAttach/{rno}")
	public void downloadDetail(@PathVariable int rno, HttpServletResponse response) {
		RaffleImage raffleImage = raffleService.getDetailImage(rno);
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

	// 관리자용 전체 회원의 참여 내역 조회
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/getAdminRaffleDetail/{mid}")
	public List<RaffleDetailRequest> getAdminRaffleDetail(@PathVariable String mid) {
		return raffleService.getAdminRaffleDetail(mid);
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/getAdminDashboard")
	public Map<String, Object> getAdminDashboard() {
		return raffleService.getAdminDashboard();
	}

	// 래플 모니터링
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/getRaffleMonitor")
	public RaffleRequest getRaffleMonitor(@RequestParam int rno) {
		return raffleService.getRaffleMonitor(rno);
	}

	// 모니터 참여자 리스트
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("/getMemberMonitor")
	public Map<String, Object> getMemberMonitor(@RequestParam int rno, @RequestParam int pageNo) {
		int totalRows = raffleService.countEntryMember(rno);

		Pager pager = new Pager(5, 5, totalRows, pageNo);

		List<RaffleDetailRequest> list = raffleService.getMemberMonitor(rno, pager);

		Map<String, Object> map = new HashMap<>();
		map.put("member", list);
		map.put("pager", pager);
		return map;
	}
}

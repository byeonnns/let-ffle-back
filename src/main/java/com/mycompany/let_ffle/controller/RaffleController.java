package com.mycompany.let_ffle.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.mycompany.let_ffle.dto.TimeMission;
import com.mycompany.let_ffle.dto.request.RaffleRequest;
import com.mycompany.let_ffle.service.RaffleService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/raffle")

public class RaffleController {

	@Autowired
	private RaffleService raffleService;

	@PostMapping("/createRaffle")
	public RaffleRequest createRaffle(RaffleRequest raffleRequest) {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		TimeMission timeMission = new TimeMission();
		timeMission.setTstartedat(timestamp);
		timeMission.setTfinishedat(timestamp);
		raffleRequest.setTimeMission(timeMission);
		raffleRequest.getRaffle().setRstartedat(timestamp);
		raffleRequest.getRaffle().setRfinishedat(timestamp);

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
		 List<RaffleRequest> list =  raffleService.getListForUser();
		 //포스트맨에서 이미지의 데이터이름 너무길기때문에 향상된 포문으로 rr:list 이런 형식으로 작성한이유는 리스트가 끝날때까지 모든요소에 레플이미지 객체를 널처리해주기 위해 사용(rr는 변수이름)
		 for(RaffleRequest rr : list) {
			 rr.setRaffleImage(null);
		 }
		 return list;
	}

	@GetMapping("/readRaffle/{rno}")
	public RaffleRequest readRaffle(@PathVariable int rno) {
		RaffleRequest raffleRequest = raffleService.getRaffle(rno);
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
}

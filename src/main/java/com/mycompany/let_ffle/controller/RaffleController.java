package com.mycompany.let_ffle.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.let_ffle.dto.Raffle;
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
	public RaffleRequest createRaffle(@RequestBody RaffleRequest raffleRequest) {
		if (raffleRequest.getRaffleImage().getRthumbnailattach() != null
				&& raffleRequest.getRaffleImage().getRthumbnailattach().isEmpty()) {
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

		if (raffleRequest.getRaffleImage().getRdetailattach() != null
				&& raffleRequest.getRaffleImage().getRdetailattach().isEmpty()) {
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

		if (raffleRequest.getRaffleImage().getRgiftattach() != null
				&& raffleRequest.getRaffleImage().getRgiftattach().isEmpty()) {
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

		raffleService.insertRaffle(raffleRequest);
		
		return raffleRequest;
	}

	@GetMapping("/getRaffleList")
	public List<RaffleRequest> getRaffleList() {
		return null;
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

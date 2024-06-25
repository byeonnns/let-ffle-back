package com.mycompany.let_ffle.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.let_ffle.dto.Inquiry;
import com.mycompany.let_ffle.service.NoticeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/notice")
public class NoticeController {

	@Autowired
	private NoticeService noticeService;

	// 문의 등록하기
	@PostMapping("/createInquiry")
	public Inquiry createInquiry(Inquiry inquiry) {
		if (inquiry.getIattach() != null && !inquiry.getIattach().isEmpty()) {
			MultipartFile mf = inquiry.getIattach();

			inquiry.setIattachoname(mf.getOriginalFilename());
			inquiry.setIattachtype(mf.getContentType());
			try {
				inquiry.setIattachdata(mf.getBytes());
			} catch (IOException e) {

			}
		}

		noticeService.insertInquiry(inquiry);
		inquiry.setIattach(null);	
		inquiry.setIattachdata(null);
		return inquiry;
	}
}

package com.mycompany.let_ffle.controller;

import java.io.IOException;
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

import com.mycompany.let_ffle.dto.Inquiry;
import com.mycompany.let_ffle.dto.Notice;
import com.mycompany.let_ffle.dto.Pager;
import com.mycompany.let_ffle.service.NoticeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/notice")
public class NoticeController {
	@Autowired
	private NoticeService noticeService;

	/* 1:1 문의 */
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
	
	@GetMapping("/getInquiryList")
	public Map<String, Object> getInquiryList(@RequestParam(defaultValue = "1") int pageNo) {
		int totalRows = noticeService.getCount();
		Pager pager = new Pager(5, 5, totalRows, pageNo);
		List<Inquiry> list = noticeService.getInquiryList(pager);
		Map<String, Object> map = new HashMap<>();
		map.put("Inquiry", list);
		map.put("Pager", pager);
		return map;
	}
	
	@GetMapping("/readInquiry/{ino}")
	public Inquiry readInquiry(@PathVariable int ino){
		Inquiry inquiry = noticeService.getInquiry(ino);
		inquiry.setIattachdata(null);
		return inquiry ;
	}
	
	@PutMapping("/updateInquiry")
	public Inquiry updateInquiry(Inquiry inquiry) {
		if (inquiry.getIattach() != null && !inquiry.getIattach().isEmpty()) {
			MultipartFile mf = inquiry.getIattach();

			inquiry.setIattachoname(mf.getOriginalFilename());
			inquiry.setIattachtype(mf.getContentType());
			try {
				inquiry.setIattachdata(mf.getBytes());
			} catch (IOException e) {
			}
		}
		noticeService.updateInquiry(inquiry);
		inquiry.setIattach(null);
		inquiry.setIattachdata(null);
		return inquiry;
	}
	
	/* 공지사항 */
	@PostMapping("/createNotice")
	public Notice createNotice(Notice notice) {
		
		if (notice.getNattach() != null && !notice.getNattach().isEmpty()) {
			MultipartFile mf = notice.getNattach();
			notice.setNattachoname(mf.getOriginalFilename());
			notice.setNattachtype(mf.getContentType());
			try {
				notice.setNattachdata(mf.getBytes());
			} catch (IOException e) {
			}
		}
		noticeService.insertNotice(notice);
		notice.setNattach(null);
		notice.setNattachdata(null);
		return notice;
	}
	
}

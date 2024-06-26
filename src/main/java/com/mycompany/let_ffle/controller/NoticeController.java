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

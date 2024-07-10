package com.mycompany.let_ffle.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

	// 공지사항 목록 가져오기
	@GetMapping("/getNoticeList")
	public Map<String, Object> getNoticeList(@RequestParam(defaultValue = "1") int pageNo, 
			String mainCategory ,@RequestParam(required = false, defaultValue = "전체") String subCategory) {
		int totalRows = noticeService.getNoticeCount(mainCategory ,subCategory);
		Pager pager = new Pager(5, 5, totalRows, pageNo);
		List<Notice> list = noticeService.getNoticeList(pager, mainCategory ,subCategory);
		
		// JSON 응답 생성
		Map<String, Object> map = new HashMap<>();
		map.put("Notice", list);
		map.put("Pager", pager);
		map.put("Subcategory", subCategory);

		return map;
	}

	// 공지사항 등록
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PostMapping("/createNotice")
	public Notice createNotice(Notice notice) {
		// 첨부파일이 포함되어 있는지 검사
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

		// JSON 응답 생성 : 변환되지 않는 데이터는 null 처리
		notice.setNattach(null);
		notice.setNattachdata(null);

		return notice;
	}

	// 공지사항 읽기
	@GetMapping("/readNotice/{nno}")
	public Notice readNotice(@PathVariable int nno) {
		Notice notice = noticeService.readNotice(nno);
		if (notice.getNattach() != null) {			
			notice.setNattach(null);
			notice.setNattachdata(null);
		}
		return notice;
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PutMapping("/updateNotice")
	public Notice updateNotice(Notice notice) {
		if (notice.getNattach() != null && !notice.getNattach().isEmpty()) {
			MultipartFile mf = notice.getNattach();
			notice.setNattachoname(mf.getOriginalFilename());
			notice.setNattachtype(mf.getContentType());
			try {
				notice.setNattachdata(mf.getBytes());
			} catch (IOException e) {

			}
		}
		noticeService.updateNotice(notice);

		notice.setNattach(null);
		notice.setNattachdata(null);

		return notice;
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@DeleteMapping("/deleteNotice")
	public Notice deleteNotice(Notice notice) {
		noticeService.deleteNotice(notice);
		return notice;
	}
}

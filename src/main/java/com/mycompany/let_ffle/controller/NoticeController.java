package com.mycompany.let_ffle.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.let_ffle.dto.Notice;
import com.mycompany.let_ffle.service.NoticeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/notice")
public class NoticeController {
	@Autowired
	private NoticeService noticeService;

	// 공지사항 등록
	@PostMapping("/createNotice")
	public Notice createNotice(Notice notice) {
		// 첨부파일이 포함되어 있는지 검사
		if (notice.getNattach() != null && !notice.getNattach().isEmpty()) {
			// 첨부파일이 포함된 경우
			// MultipartFile 객체에 첨부파일 데이터를 저장
			MultipartFile mf = notice.getNattach();

			// Inquiry 객체의 iattachoname, Iattachtype 필드 값으로 각각 파일명과 확장자명을 저장
			notice.setNattachoname(mf.getOriginalFilename());
			notice.setNattachtype(mf.getContentType());

			// Inquiry 객체의 iattachdata 필드에 바이너리 데이터를 저장
			try {
				notice.setNattachdata(mf.getBytes());
			} catch (IOException e) {
			}
		}

		// 변경된 값으로 Notice를 저장하도록 noticeService의 insertNotice() 호출
		// 매개변수로 Notice 객체를 넘겨줌
		noticeService.insertNotice(notice);

		// JSON 응답에는 바이너리 데이터가 출력되지 않음
		// 따라서 해당 필드값을 null로 변경 후 응답을 반환
		notice.setNattach(null);
		notice.setNattachdata(null);

		return notice;
	}

	@PutMapping("/updateNotice")
	public Notice updateNotice(Notice notice) {
		if (notice.getNattach() != null && notice.getNattach().isEmpty()) {
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

}

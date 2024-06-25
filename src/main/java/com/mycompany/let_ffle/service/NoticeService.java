package com.mycompany.let_ffle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.let_ffle.dao.NoticeDao;
import com.mycompany.let_ffle.dto.Inquiry;

@Service
public class NoticeService {
	@Autowired
	private NoticeDao noticeDao;

	public void insertInquiry(Inquiry inquiry) {
		
		noticeDao.insertInquiry(inquiry);
		
	}
	
}

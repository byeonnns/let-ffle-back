package com.mycompany.let_ffle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.let_ffle.dao.NoticeDao;
import com.mycompany.let_ffle.dto.Inquiry;
import com.mycompany.let_ffle.dto.Notice;
import com.mycompany.let_ffle.dto.Pager;

@Service
public class NoticeService {
	@Autowired
	private NoticeDao noticeDao;

	/* 1:1 문의 */
	public void insertInquiry(Inquiry inquiry) {
		noticeDao.insertInquiry(inquiry);
	}
	public int getCount() {
		return noticeDao.InquiryCount();
	}
	public List<Inquiry> getInquiryList(Pager pager) {
		return noticeDao.selectByPage(pager);
	}

	public Inquiry getInquiry(int ino) {	
		return noticeDao.readInquiry(ino);
	}
	public int updateInquiry(Inquiry inquiry) {
		return noticeDao.updateInquiry(inquiry);
	}

	
	/* 공지사항 */
	public void insertNotice(Notice notice) {
		noticeDao.insertNotice(notice);		
	}

}

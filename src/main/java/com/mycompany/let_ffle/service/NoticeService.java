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

	/* 공지사항 */
	public void insertNotice(Notice notice) {
		noticeDao.insertNotice(notice);
	}

	public void updateNotice(Notice notice) {

		noticeDao.updateNotice(notice);

	}

	public void deleteNotice(Notice notice) {

		noticeDao.deleteNotice(notice);
	}

	public int getNoticeCount(String mainCategory, String subCategory) {
		return noticeDao.noticeCount(mainCategory, subCategory);
	}

	public List<Notice> getNoticeList(Pager pager, String mainCategory, String subCategory) {
		return noticeDao.selectByPage(pager, mainCategory, subCategory);
	}

	public Notice readNotice(int nno) {
		return noticeDao.selectByNno(nno);
	}

}

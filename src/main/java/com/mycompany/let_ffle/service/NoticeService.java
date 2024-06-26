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

	public int getCount() {
		// TODO Auto-generated method stub
		return noticeDao.noticeCount();
	}

	public List<Notice> getNoticeList(Pager pager) {
		// TODO Auto-generated method stub
		return noticeDao.selectByPage(pager);
	}

	public Notice readNotice(int nno) {
		// TODO Auto-generated method stub
		return noticeDao.selectByNno(nno);
	}

}

package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.Inquiry;
import com.mycompany.let_ffle.dto.Notice;
import com.mycompany.let_ffle.dto.Pager;

@Mapper
public interface NoticeDao {

	/* 1:1 문의 */
	public int insertInquiry(Inquiry inquiry);
	public int InquiryCount();
	public List<Inquiry> selectByPage(Pager pager);
	public Inquiry readInquiry(int ino);
	public int updateInquiry(Inquiry inquiry);

	/* 공지사항 */
	public int insertNotice(Notice notice);
}

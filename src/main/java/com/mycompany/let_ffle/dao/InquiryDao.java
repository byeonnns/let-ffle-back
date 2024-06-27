package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.Inquiry;
import com.mycompany.let_ffle.dto.Pager;

@Mapper
public interface InquiryDao {
	/* 1:1 문의 */
	public int insertInquiry(Inquiry inquiry);
	public int InquiryCount();
	public List<Inquiry> selectByPage(Pager pager);
	public Inquiry readInquiry(int ino);
	public int updateInquiry(Inquiry inquiry);
	public void updateInquiryReply(int ino, String ireply);
}

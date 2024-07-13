package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.Inquiry;
import com.mycompany.let_ffle.dto.Pager;

@Mapper
public interface InquiryDao {
	public int insertInquiry(Inquiry inquiry);
	
	public Inquiry readInquiry(int ino);
	
	public int updateInquiry(Inquiry inquiry);
	
	public void updateInquiryReply(int ino, String ireply);
	
	public int getInquiryCount(String mid);
	
	public List<Inquiry> selectByPage(Pager pager, String mid);
	
	public Inquiry selectInquiryIno(int ino);
	
	public int getUserInquiryCount(String mid, String role);
	
	public List<Inquiry> getUserInquiryList(Pager pager);
}

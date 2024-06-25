package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.Inquiry;
import com.mycompany.let_ffle.dto.Pager;

@Mapper
public interface NoticeDao {

	void insertInquiry(Inquiry inquiry);

	int InquiryCount();

	List<Inquiry> selectByPage(Pager pager);

	public Inquiry readInquiry(int ino);

}

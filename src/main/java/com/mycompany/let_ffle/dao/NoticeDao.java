package com.mycompany.let_ffle.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.Inquiry;

@Mapper
public interface NoticeDao {

	void insertInquiry(Inquiry inquiry);

}

package com.mycompany.let_ffle.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.Member;

@Mapper
public interface MemberDao {

	void insert(Member member);
	//insert 는 변수의 명이며 MemberDto에 담겨져 있는 값들을 가져오기 위해서 선언하였다.
	
	Member selectByMid(String mid);
	
	void delete(String mid);
}

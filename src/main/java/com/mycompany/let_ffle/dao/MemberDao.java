package com.mycompany.let_ffle.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.Member;

@Mapper
public interface MemberDao {

	Member selectByMid(String username);

}

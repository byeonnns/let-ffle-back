package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.let_ffle.dto.Winner;

@Mapper
public interface WinnerDao {

	public int insertWinner(Winner winner);

	public Winner selectWinnerDetail(int rno);

	public List<Winner> selectWinnerDetailList(String mid, String role);
	
	

	

	
	

}

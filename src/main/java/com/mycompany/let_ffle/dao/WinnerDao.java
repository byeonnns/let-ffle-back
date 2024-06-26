package com.mycompany.let_ffle.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.Winner;

@Mapper
public interface WinnerDao {

	public int insertWinner(Winner winner);
	

}

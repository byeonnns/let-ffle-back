package com.mycompany.let_ffle.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.Board;

@Mapper
public interface BoardDao {

	public int insertBoard(Board board);

	public void updateBoard(Board board);

	public void enabledBoard(int bno);

	public Board readBoard(int bno);

	

}

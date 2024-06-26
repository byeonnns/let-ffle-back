package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.Board;
import com.mycompany.let_ffle.dto.Pager;

@Mapper
public interface BoardDao {

	public int insertBoard(Board board);

	public void updateBoard(Board board);

	public void enabledBoard(int bno);

	public Board readBoard(int bno);

	public int BoardCount();

	public List<Board> SelectByBoardList(Pager pager);

	
	

	

}

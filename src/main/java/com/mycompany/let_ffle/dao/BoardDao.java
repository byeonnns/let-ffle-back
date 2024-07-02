package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.Board;
import com.mycompany.let_ffle.dto.Pager;

@Mapper
public interface BoardDao {

	public int insertBoard(Board board);

	public void updateBoard(Board board);

	public void deleteBoard(int bno);

	public Board readBoard(int bno);

	public int boardCount();

	public List<Board> selectByBoardList(Pager pager);

	public void updatehitCount(int bno);

	public List<Board> getBoardTitleList(Pager pager, String mid);

	public Board selectBoardByBno(int bno);
	
	



	
	

	

}

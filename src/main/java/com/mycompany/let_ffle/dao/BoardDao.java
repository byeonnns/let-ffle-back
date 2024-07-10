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

	public int getBoardCountByWord(String searchType, String word);
	
	public int getBoardCountByCategory(String category);
	
	public Board getBoardByBno(int bno);
	
	public List<Board> getBoardList(Pager pager, String searchType, String word);

	public List<Board> getBoardListByCategory(Pager pager, String category);
	
	public void updateHitCount(int bno);

	public List<Board> getBoardTitleList(Pager pager, String mid);
}

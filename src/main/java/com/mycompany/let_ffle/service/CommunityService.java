package com.mycompany.let_ffle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.let_ffle.dao.BoardDao;
import com.mycompany.let_ffle.dto.Board;
import com.mycompany.let_ffle.dto.Pager;

@Service
public class CommunityService {
	@Autowired
	private BoardDao boardDao;

	public void insertBoard(Board board) {
		boardDao.insertBoard(board);
	}

	public void updateBoard(Board board) {
		boardDao.updateBoard(board);
	}

	public void enabledBoard(int bno) {
		boardDao.enabledBoard(bno);
	}

	public Board readBoard(int bno) {
		return boardDao.readBoard(bno);
	}

	public int BoardCount() {
		return boardDao.BoardCount();
	}

	public List<Board> SelectByBoardList(Pager pager) {
		return boardDao.SelectByBoardList(pager);
	}

	
}

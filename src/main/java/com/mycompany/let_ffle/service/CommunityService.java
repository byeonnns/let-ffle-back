package com.mycompany.let_ffle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.let_ffle.dao.BoardDao;
import com.mycompany.let_ffle.dao.BoardCommentDao;
import com.mycompany.let_ffle.dto.Board;
import com.mycompany.let_ffle.dto.BoardComment;
import com.mycompany.let_ffle.dto.Pager;

@Service
public class CommunityService {
	@Autowired
	private BoardDao boardDao;
	@Autowired
	private BoardCommentDao boardCommentDao;

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
		// 게시글 조회 요청이 들어오면 조회수를 1 증가시킴
		boardDao.updatehitCount(bno);
		return boardDao.readBoard(bno);
	}

	public int boardCount() {
		return boardDao.boardCount();
	}

	public List<Board> selectByBoardList(Pager pager) {
		return boardDao.selectByBoardList(pager);
	}

	
	//댓글
	public int getCommentCount(int bno) {

		return boardCommentDao.commentCount(bno);
	}

	public List<BoardComment> getCommentList(Pager pager, int bno) {
		
		return boardCommentDao.selectByCommentList(pager, bno);
	}

	public void deleteComment(int cno) {

		boardCommentDao.deleteComment(cno);
	}

	public void insertComment(BoardComment boardComment) {
		
		boardCommentDao.insertComment(boardComment);
	}



	
}

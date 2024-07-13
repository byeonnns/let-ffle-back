package com.mycompany.let_ffle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.let_ffle.dao.BoardCommentDao;
import com.mycompany.let_ffle.dao.BoardDao;
import com.mycompany.let_ffle.dto.Board;
import com.mycompany.let_ffle.dto.BoardComment;
import com.mycompany.let_ffle.dto.Pager;
import com.mycompany.let_ffle.dto.request.BoardCommentRequest;

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

	public void deleteBoard(int bno) {
		boardDao.deleteBoard(bno);
	}

	public Board readBoard(int bno) {
		// 게시글 조회 요청이 들어오면 조회수를 1 증가시킴
		boardDao.updateHitCount(bno);
		return boardDao.readBoard(bno);
	}

	public int boardCount() {
		return boardDao.boardCount();
	}

	public List<Board> getBoardList(Pager pager, String searchType, String word) {
		return boardDao.getBoardList(pager, searchType, word);
	}
	
	//댓글
	public int getCommentCount(int bno) {
		return boardCommentDao.getCommentCount(bno);
	}

	public List<BoardCommentRequest> getCommentList(int bno) {
		return boardCommentDao.getCommentList(bno);
	}

	public void deleteComment(int cno) {
		boardCommentDao.deleteComment(cno);
	}

	public void insertComment(BoardComment boardComment) {
		boardCommentDao.insertComment(boardComment);
	}

	public Board getBoardByBno(int bno) {
		return boardDao.getBoardByBno(bno);
	}

	public int getBoardCountByWord(String searchType, String word) {
		return boardDao.getBoardCountByWord(searchType, word);
	}

	public int getBoardCountByCategory(String category) {
		return boardDao.getBoardCountByCategory(category);
	}

	public List<Board> getBoardListByCategory(Pager pager, String category) {
		return boardDao.getBoardListByCategory(pager, category);
	}
}

package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.let_ffle.dto.BoardComment;
import com.mycompany.let_ffle.dto.Pager;

@Mapper
public interface BoardCommentDao {
	
	//게시물에 등록된 댓글갯수
	public int commentCount(int bno);

	public List<BoardComment> selectByCommentList(Pager pager, int bno);

	public void deleteComment(int cno);

	public void insertComment(BoardComment boardComment);
	
	//마이페이지 내가 작성한 댓글 목록조회
	public int getBoardCommentCount(String mid);
}

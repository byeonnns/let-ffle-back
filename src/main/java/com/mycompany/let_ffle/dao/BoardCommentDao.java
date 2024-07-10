package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.BoardComment;

@Mapper
public interface BoardCommentDao {
	public List<BoardComment> getCommentList(int bno);

	public void insertComment(BoardComment boardComment);

	public int getCommentCount(int bno);

	public void deleteComment(int cno);

	public int getBoardCommentCount(String mid);
}

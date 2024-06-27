package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.let_ffle.dto.BoardComment;
import com.mycompany.let_ffle.dto.Pager;

@Mapper
public interface BoardCommentDao {

	public int commentCount();

	public List<BoardComment> selectByCommentList( Pager pager , int bno);

	public void deleteComment(int cno);

	public void insertComment(BoardComment boardComment);



}

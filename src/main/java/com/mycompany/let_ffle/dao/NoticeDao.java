package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.let_ffle.dto.Notice;
import com.mycompany.let_ffle.dto.Pager;

@Mapper
public interface NoticeDao {
	
	/* 공지사항 */
	public int insertNotice(Notice notice);

	public void updateNotice(Notice notice);

	public void deleteNotice(int nno);

	public int noticeCount(String mainCategory, String subCategory);

	public List<Notice> selectByPage(Pager pager, String mainCategory, String subCategory);

	public Notice selectByNno(int nno);
	
}

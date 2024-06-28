package com.mycompany.let_ffle.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.let_ffle.dto.BerryHistory;
import com.mycompany.let_ffle.dto.Board;
import com.mycompany.let_ffle.dto.Inquiry;
import com.mycompany.let_ffle.dto.Member;
import com.mycompany.let_ffle.dto.Pager;

@Mapper
public interface MemberDao {

	void insert(Member member);
	// insert 는 변수의 명이며 MemberDto에 담겨져 있는 값들을 가져오기 위해서 선언하였다.

	Member selectByMid(String mid);

	void delete(String mid);

	void updateMphone(Member member);
	// changeMphone = 메소드 명 이며 우리는 현재 Mphone의 값을 변경시켜주기 위해 MemberDto에 담겨져 있는 값을
	// 가져오기위해 선언한다.

	// 유저의 아이디랑 비밀번호 매개변수로 주고 비밀번호를 변경함
	void updateMpassword(String mid, String mpassword);

	// 유저의 아이디와 주소를 매개변수로 주고 주소를 변경하기 위한 메소드 정의
	void updateMaddress(String mid, String maddress, String mzipcode);

	// 여기입니둥
	void login(Member member, String mid, String mpassword);

	//마이페이지 내 게시물 사용하는 것들을 페이져하기위함과 나의 아이디에 맞춰서 가져오기 때문에 pager와 mid를 선언.
	public List<Board> getMyBoardList(Pager pager, String mid);
	//마이페이지 내가 쓴 게시물의 갯수
	int getMyBoardCount(String mid);
	
	String findId(String mphone);

	int findPassword(String mname, String mid);
	
	//로그인 로직 관련
	public void updateLoginTime(String mid);
	
	Member selectLoginTime(String mid);

	public int selectBerry(String mid);
	
	void updateBerry(String mid, int mberry);

}
package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.Board;
import com.mycompany.let_ffle.dto.Member;
import com.mycompany.let_ffle.dto.Pager;
import com.mycompany.let_ffle.dto.request.RaffleDetailRequest;

@Mapper
public interface MemberDao {
	void insert(Member member);

	Member selectByMid(String mid);

	void delete(String mid);

	void updateMphone(String mid, String mphone);

	void updateMpassword(String mid, String mpassword);

	void updateMaddress(String mid, String mzipcode, String maddress);

	void login(Member member, String mid, String mpassword);

	public List<Board> getMyBoardList(Pager pager, String mid);

	int getMyBoardCount(String mid);
	
	public String findId(Member member);

	int findPassword(String mphone, String mid);
	
	public void updateLoginTime(String mid);
	
	Member selectLoginTime(String mid);

	public int selectBerry(String mid);
	
	void updateBerry(String mid, int mberry);

	int countByMid(String mid);
	
	int countByMnickname(String mnickname);
	
	int countByMphone(String mphone);

	List<Member> selectByMember(Pager pager, String searchType, String word);

	public int memberCount();

	void updateMnickname(String mid, String mnickname);

	public Member getMemberDetail(String mid);

	int memberCountByWord(String searchType, String word);

	public List<RaffleDetailRequest> getMonitorList(int rno, Pager pager);
}
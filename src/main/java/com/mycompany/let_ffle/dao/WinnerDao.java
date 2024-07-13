package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.Pager;
import com.mycompany.let_ffle.dto.Raffle;
import com.mycompany.let_ffle.dto.Winner;
import com.mycompany.let_ffle.dto.request.RaffleDetailRequest;

@Mapper
public interface WinnerDao {
	public void insertWinner(int rno, String mid);

	public List<Winner> selectWinnerDetail(int rno, Pager pager);

	public List<Raffle> selectWinnerDetailList(String mid, Pager pager, String startDate, String endDate);

	public int countWinRaffle(String mid, String startDate, String endDate);
	
	public int winnerCount();
	
	public int winnerExistenceCheck(int rno);

	public List<RaffleDetailRequest> selectByWinnerList(Pager pager, String searchType, String word);

	public void updateWinner(Winner winner);

	public int winnerCheck(int rno, String mid);

	public int winnerCountByWord(String searchType, String word);

	public int getRaffleWinnerCount(int rno);
}

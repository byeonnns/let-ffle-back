package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.Pager;
import com.mycompany.let_ffle.dto.Raffle;
import com.mycompany.let_ffle.dto.Winner;
import com.mycompany.let_ffle.dto.request.RaffleDetailRequest;

@Mapper
public interface WinnerDao {

	public int insertWinner(Winner winner);

	public Winner selectWinnerDetail(int rno);

	public List<Raffle> selectWinnerDetailList(String mid, Pager pager, String startDate, String endDate);

	public int countWinRaffle(String mid, String startDate, String endDate);
	
	public int winnerCount();

	public List<Winner> selectByWinnerList(Pager pager);
	

	
	

	
	
>>>>>>> branch 'master' of https://github.com/byeonnns/let-ffle-back

	public void updateWinner(Winner winner);

}

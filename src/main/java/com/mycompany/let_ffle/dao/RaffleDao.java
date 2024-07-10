package com.mycompany.let_ffle.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.Pager;
import com.mycompany.let_ffle.dto.Raffle;
import com.mycompany.let_ffle.dto.request.RaffleRequest;

@Mapper
public interface RaffleDao {

	public int insertRaffle(Raffle raffle);

	public Raffle selectByRno(int rno);

	public int getRaffleCount(String rcategory);
	
	public int ongoingRaffleCount(String rcategory);
	
	public int closedRaffleCount(String rcategory);

	public List<Raffle> selectByPage(Pager pager);

	public int updateRaffle(Raffle raffle);

	public Raffle deleteRaffle(int rno);

	public List<RaffleRequest> selectByRaffleListForUser(String rcategory, String sortType);

	public List<RaffleRequest> searchRaffle(String word);
	
	public Map<String, Object> getMemberRaffleDetail(String mid, Raffle raffle);
	
	public List<RaffleRequest> getNewReleaseRaffles();

	public List<RaffleRequest> getCutOffSoonRaffles();

	public RaffleRequest selectForMonitor(int rno);
}

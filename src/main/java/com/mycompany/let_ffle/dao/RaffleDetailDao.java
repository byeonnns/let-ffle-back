package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.let_ffle.dto.RaffleDetail;

@Mapper
public interface RaffleDetailDao {

	public RaffleDetail getRaffleEntryList(String mid);

	public void insertRaffleDetail(RaffleDetail raffleDetail);

	public RaffleDetail readRaffleDetail(RaffleDetail raffleDetail);

	public List<RaffleDetail> selectRaffleDetailList(@Param("mid") String mid, @Param("role") String role);

	public void updateRdtMissionCleard(int rno, String mid, String rdtMissionCleared);


	
	
	

	
}

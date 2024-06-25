package com.mycompany.let_ffle.dao;

import java.math.BigDecimal;
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

	public int raffleCount();

	public List<RaffleRequest> selectByRaffleListForUser();

	public List<Raffle> selectByPage(Pager pager);
}

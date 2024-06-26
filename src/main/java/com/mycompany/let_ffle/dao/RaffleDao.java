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

	// raffle(dto)의 값들을 생성하는 메소드
	public int insertRaffle(Raffle raffle);

	// rno 래플 번호를 통해 해당 래플dto를 가져옴
	public Raffle selectByRno(int rno);

	public int raffleCount();

	public List<RaffleRequest> selectByRaffleListForUser();

	public List<Raffle> selectByPage(Pager pager);

}

package com.mycompany.let_ffle.dao;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.Raffle;

@Mapper
public interface RaffleDao {

	public int insertRaffle(Raffle raffle);

	public Raffle selectByRno(int rno);

	public Map<String, BigDecimal> selectBySeqRno();
}

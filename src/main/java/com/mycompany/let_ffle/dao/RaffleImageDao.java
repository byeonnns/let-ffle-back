package com.mycompany.let_ffle.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.RaffleImage;

@Mapper
public interface RaffleImageDao {

	public int insertRaffleImage(RaffleImage raffleImage);

}

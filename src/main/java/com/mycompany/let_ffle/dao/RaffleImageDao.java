package com.mycompany.let_ffle.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.RaffleImage;

@Mapper
public interface RaffleImageDao {

	public int insertRaffleImage(RaffleImage raffleImage);

	public RaffleImage selectByRno(int rno);

	public int updateRaffleImage(RaffleImage raffleImage);

	public RaffleImage getDetailImage(int rno);

	public RaffleImage getThumbnailImage(int rno);

	public RaffleImage getGiftImage(int rno);

}

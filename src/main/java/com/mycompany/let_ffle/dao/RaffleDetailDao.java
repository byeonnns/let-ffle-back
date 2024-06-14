package com.mycompany.let_ffle.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.RaffleDetail;

@Mapper
public interface RaffleDetailDao {

	public RaffleDetail getRaffleEntryList(String mid);
}

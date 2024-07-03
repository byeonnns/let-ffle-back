package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.BerryHistory;

@Mapper
public interface BerryHistoryDao {

	void insertBerryHistory(BerryHistory berryHistory);

	List<BerryHistory> selectByMid(String mid);



}

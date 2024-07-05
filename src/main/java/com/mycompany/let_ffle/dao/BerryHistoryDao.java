package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.BerryHistory;
import com.mycompany.let_ffle.dto.Pager;

@Mapper
public interface BerryHistoryDao {

	void insertBerryHistory(BerryHistory berryHistory);

	List<BerryHistory> selectByMid(String mid, Pager pager, String option);

	public int countTotalBH(String mid);
	public int countSaveBH(String mid);
	public int countUseBH(String mid);
}

package com.mycompany.let_ffle.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.TimeMission;

@Mapper
public interface TimeMissionDao {

	public int insertTimeMisson(TimeMission timeMission);
	
}

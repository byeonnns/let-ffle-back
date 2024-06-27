package com.mycompany.let_ffle.dao;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.QuizMission;
import com.mycompany.let_ffle.dto.Raffle;

@Mapper
public interface QuizMissionDao {

	public int insertQuizMisson(QuizMission quizMission);

	public QuizMission selectByRno(int rno);

}

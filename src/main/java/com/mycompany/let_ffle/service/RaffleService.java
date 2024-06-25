package com.mycompany.let_ffle.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.let_ffle.dao.QuizMissionDao;
import com.mycompany.let_ffle.dao.RaffleDao;
import com.mycompany.let_ffle.dao.RaffleImageDao;
import com.mycompany.let_ffle.dao.TimeMissionDao;
import com.mycompany.let_ffle.dto.request.RaffleRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RaffleService {
	@Autowired
	RaffleDao raffleDao;
	@Autowired
	RaffleImageDao raffleImageDao;
	@Autowired
	TimeMissionDao timeMissionDao;
	@Autowired
	QuizMissionDao quizMissionDao;

	public void insertRaffle(RaffleRequest raffleRequest) {
		raffleDao.insertRaffle(raffleRequest.getRaffle());
		raffleImageDao.insertRaffleImage(raffleRequest.getRaffleImage());

		if (raffleRequest.getTimeMission() != null) {
			timeMissionDao.insertTimeMisson(raffleRequest.getTimeMission());
		} else {
			quizMissionDao.insertQuizMisson(raffleRequest.getQuizMission());
		}
	}

	public RaffleRequest getRaffle(int rno) {
		RaffleRequest raffleRequest = new RaffleRequest();
		raffleRequest.setRaffle(raffleDao.selectByRno(rno));
		raffleRequest.setTimeMission(timeMissionDao.selectByRno(rno));
		return raffleRequest;
	}
}

package com.mycompany.let_ffle.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.let_ffle.dao.QuizMissionDao;
import com.mycompany.let_ffle.dao.RaffleDao;
import com.mycompany.let_ffle.dao.RaffleDetailDao;
import com.mycompany.let_ffle.dao.RaffleImageDao;
import com.mycompany.let_ffle.dao.TimeMissionDao;
import com.mycompany.let_ffle.dao.WinnerDao;
import com.mycompany.let_ffle.dto.Pager;
import com.mycompany.let_ffle.dto.Raffle;
import com.mycompany.let_ffle.dto.RaffleDetail;
import com.mycompany.let_ffle.dto.Winner;
import com.mycompany.let_ffle.dto.request.RaffleRequest;

@Service
public class RaffleService {
	@Autowired
	RaffleDao raffleDao;
	@Autowired
	RaffleImageDao raffleImageDao;
	@Autowired
	TimeMissionDao timeMissionDao;
	@Autowired
	QuizMissionDao quizMissionDao;
	@Autowired
	RaffleDetailDao raffleDetailDao;
	@Autowired
	WinnerDao winnerDao;

	public void insertRaffle(RaffleRequest raffleRequest) {
		raffleDao.insertRaffle(raffleRequest.getRaffle());
		raffleImageDao.insertRaffleImage(raffleRequest.getRaffleImage());

		//raffle(dto)의 미션타입이 time이라면 timeMissionDao를 호출해 timeMission을 생성
		if (raffleRequest.getRaffle().getRmissiontype().equals("time")) {
			timeMissionDao.insertTimeMisson(raffleRequest.getTimeMission());
		// raffle(dto)의 미션타입이 quiz이라면 quizMissionDao를 호출해 quizMission을 생성
		} else if (raffleRequest.getRaffle().getRmissiontype().equals("quiz")) {
			quizMissionDao.insertQuizMisson(raffleRequest.getQuizMission());
		}
	}

	// raffleRequest 객체를 생성 후 래플의 번호와 해당 래플의 미션타입을 불러와 raffleRequest 객체에 반환 후 리턴
	public RaffleRequest getRaffle(int rno) {
		RaffleRequest raffleRequest = new RaffleRequest();
		raffleRequest.setRaffle(raffleDao.selectByRno(rno));
		if (raffleRequest.getRaffle().getRmissiontype().equals("time")) {
			timeMissionDao.insertTimeMisson(raffleRequest.getTimeMission());
		} else if(raffleRequest.getRaffle().getRmissiontype().equals("quiz")) {
			quizMissionDao.insertQuizMisson(raffleRequest.getQuizMission());
		}
		raffleRequest.setTimeMission(timeMissionDao.selectByRno(rno));
		return raffleRequest;
	}
	
	public int getCount() {
		return raffleDao.raffleCount();
	}

	public List<Raffle> getListForAdmin(Pager pager) {
		return raffleDao.selectByPage(pager);
	}

	public List<RaffleRequest> getListForUser() {
		return raffleDao.selectByRaffleListForUser();
	}

	public void insertRaffleDetail(RaffleDetail raffleDetail) {
		raffleDetailDao.insertRaffleDetail(raffleDetail);
	}

	public void insertWinner(Winner winner) {
		winnerDao.insertWinner(winner);
	}

	public RaffleDetail readRaffleDetail(RaffleDetail raffleDetail) {
		return raffleDetailDao.readRaffleDetail(raffleDetail);
	}

	

	

}

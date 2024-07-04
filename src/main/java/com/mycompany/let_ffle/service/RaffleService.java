package com.mycompany.let_ffle.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.let_ffle.dao.BerryHistoryDao;
import com.mycompany.let_ffle.dao.MemberDao;
import com.mycompany.let_ffle.dao.QuizMissionDao;
import com.mycompany.let_ffle.dao.RaffleDao;
import com.mycompany.let_ffle.dao.RaffleDetailDao;
import com.mycompany.let_ffle.dao.RaffleImageDao;
import com.mycompany.let_ffle.dao.TimeMissionDao;
import com.mycompany.let_ffle.dao.WinnerDao;
import com.mycompany.let_ffle.dto.BerryHistory;
import com.mycompany.let_ffle.dto.Pager;
import com.mycompany.let_ffle.dto.Raffle;
import com.mycompany.let_ffle.dto.RaffleDetail;
import com.mycompany.let_ffle.dto.RaffleImage;
import com.mycompany.let_ffle.dto.Winner;
import com.mycompany.let_ffle.dto.request.RaffleDetailRequest;
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
	@Autowired
	RaffleDetailDao raffleDetailDao;
	@Autowired
	WinnerDao winnerDao;
	@Autowired
	BerryHistoryDao berryHistoryDao;
	@Autowired
	MemberDao memberDao;
	

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
	public RaffleRequest readRaffle(int rno) {
		RaffleRequest raffleRequest = new RaffleRequest();
		raffleRequest.setRaffle(raffleDao.selectByRno(rno));
		if (raffleRequest.getRaffle().getRmissiontype().equals("time")) {
			raffleRequest.setTimeMission(timeMissionDao.selectByRno(rno));
		} else if(raffleRequest.getRaffle().getRmissiontype().equals("quiz")) {
			raffleRequest.setQuizMission(quizMissionDao.selectByRno(rno));
		}
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

	public Map<String, Object> getRaffleDetailList(String mid, String role, int pageNo, String status, String startDate, String endDate) {
		Map<String, Object> map = new HashMap<>();
		int myTotalRaffle = raffleDetailDao.selectTotalRaffle(mid, role, startDate, endDate);
		int myOngoingRaffle = raffleDetailDao.selectOngoingRaffle(mid, role, startDate, endDate);
		int myClosedRaffle = raffleDetailDao.selectClosedRaffle(mid, role, startDate, endDate);
		map.put("myTotalRaffle", myTotalRaffle);
		map.put("myOngoingRaffle", myOngoingRaffle);
		map.put("myClosedRaffle", myClosedRaffle);
		Pager pager = new Pager(1, 1, 1, 1);
		
		if(status.equals("Ongoing")) {
			pager = new Pager(5, 5, myOngoingRaffle, pageNo);
		}
		else if(status.equals("Closed")) {
			pager = new Pager(5, 5, myClosedRaffle, pageNo);
		}
		else {
			pager = new Pager(5, 5, myTotalRaffle, pageNo);
		}
		
		List<RaffleDetailRequest> list = raffleDetailDao.selectRaffleDetailRequest(mid, pager, status, startDate, endDate);
		for(RaffleDetailRequest rdr : list) {
			
			if(rdr.getRaffleDetail().getRdtmissioncleared().equals("PASS"))
				rdr.getRaffleDetail().setRdtmissioncleared("완료");
			else if(rdr.getRaffleDetail().getRdtmissioncleared().equals("FAIL"))
				rdr.getRaffleDetail().setRdtmissioncleared("실패");
			else if(rdr.getRaffleDetail().getRdtmissioncleared().equals("PEND"))
				rdr.getRaffleDetail().setRdtmissioncleared("대기 중");
			
			if(rdr.getRaffle().getRfinishedat().toLocalDateTime().toLocalDate().isAfter(LocalDate.now()))
				rdr.setNowStatus("진행 중");
			else
				rdr.setNowStatus("종료");
			
			rdr.setProbability(computeProbability(mid, rdr.getRaffle().getRno()));
		}
		map.put("RaffleDetailRequest", list);
		map.put("pager", pager);
		
		return map;
	}

	public Winner readWinnerDetail(int rno) {
		return winnerDao.selectWinnerDetail(rno);
	}

	

	// 매개변수로 rno, mid, manswer(회원이 제출할 문제 답안) 받음
	public void updateRdtMissionCleared(int rno, String mid, String manswer) {
		// rno를 통해 raffle(dto)의 미션타입을 rMissionType 변수에 반환
		String rMissionType = raffleDao.selectByRno(rno).getRmissiontype();
		// 미션타입이 퀴즈미션이면 rno를 통해 quizMission(dto)의 qanswer(퀴즈의 정답)을 가져와 qanswer 변수에 반환
		if(rMissionType.equals("quiz")) {
			String qanswer = quizMissionDao.selectByRno(rno).getQanswer();
			// 퀴즈 정답과 회원이 제출한 정답이 같으면 매개변수로 rno,mid,pass를 넘겨 데이터베이스 정보를 수정하도록함
			if(qanswer.equals(manswer)) {
				raffleDetailDao.updateRdtMissionCleard(rno, mid, "PASS");
				// 정답을 틀릴 시 래플번호, 회원아이디, fail 정보를 넘겨 데이터베이스 정보를 수정하도록 함
			} else {
				raffleDetailDao.updateRdtMissionCleard(rno, mid, "FAIL");
			}
			// 미션타입이 time미션일 경우 
		} else if(rMissionType.equals("time")){
			
		}
	}
	
	public List<RaffleDetail> getMyRaffleDetailRequestList(String mid, String startdate, String enddate) {
		return raffleDetailDao.getMyRaffleDetailRequestList(mid,startdate, enddate);
	}

	public String updateRdtBerrySpend(int rno, String mid, int rdtBerrySpend) {
		int spentberry = raffleDetailDao.selectRdtBerrySpend(rno, mid);
		int mberry = memberDao.selectBerry(mid);
		//spentberry+rdtBerrySpend -한레플에 사용할수있는 베리는 최대 10개 && 내가 가지고 있는 베리 만큼만 사용 가능 
		if(spentberry+rdtBerrySpend <= 10 && rdtBerrySpend <= mberry) {
			//유효
			raffleDetailDao.updateRdtBerrySpend(rno, mid, rdtBerrySpend);
			memberDao.updateBerry(mid, -rdtBerrySpend);
			BerryHistory berryHistory = new BerryHistory(0, mid, null, -rdtBerrySpend, rno + "번 래플에 사용");
			berryHistoryDao.insertBerryHistory(berryHistory);
			return "성공";
		} else {
			return "실패";
		}
	}
	
	// 확률 계산용
	private String computeProbability(String mid, int rno) {
		Map<String, BigDecimal> pp = raffleDetailDao.readpp(rno);
		RaffleDetail myProbability = raffleDetailDao.selectRaffleDetail(mid, rno);
		int ppScore = pp.get("TOTALENTRY").intValue() * 10 + pp.get("MISSIONCLEARED").intValue() * 2 + pp.get("BERRYSPEND").intValue();
		int missionCleared = (myProbability.getRdtmissioncleared().equals("PASS") ? 2 : 0);
		double myScore = 10 + missionCleared + myProbability.getRdtberryspend();
		return String.format("%.2f", myScore/ppScore*100);
	}


	public List<Raffle> getWinnerDetailList(String mid, Pager pager, String startDate, String endDate) {
		return winnerDao.selectWinnerDetailList(mid, pager, startDate, endDate);
	}
	
	public RaffleImage readRaffleImage(int rno) {
		return raffleImageDao.selectByRno(rno);
	}

	public Raffle deleteRaffle(int rno) {
		return raffleDao.deleteRaffle(rno);
	}

	public int getWinRaffleCount(String mid, String startDate, String endDate) {
		return winnerDao.countWinRaffle(mid, startDate, endDate);
	}

}

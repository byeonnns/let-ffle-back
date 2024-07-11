package com.mycompany.let_ffle.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

import lombok.AllArgsConstructor;
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

	// 래플 등록
	public void insertRaffle(RaffleRequest raffleRequest) {
		raffleDao.insertRaffle(raffleRequest.getRaffle());
		raffleImageDao.insertRaffleImage(raffleRequest.getRaffleImage());
		// 미션 타입이 time인 경우
		if (raffleRequest.getRaffle().getRmissiontype().equals("time")) {
			timeMissionDao.insertTimeMisson(raffleRequest.getTimeMission());
			// 미션 타입이 quiz인 경우
		} else if (raffleRequest.getRaffle().getRmissiontype().equals("quiz")) {
			quizMissionDao.insertQuizMisson(raffleRequest.getQuizMission());
		}
	}

	// 래플 수정
	public void updateRaffle(RaffleRequest raffleRequest) {
		raffleDao.updateRaffle(raffleRequest.getRaffle());
		if(raffleRequest.getRaffleImage() != null)
			raffleImageDao.updateRaffleImage(raffleRequest.getRaffleImage());
		if (raffleRequest.getRaffle().getRmissiontype().equals("time")) {
			timeMissionDao.updateTimeMisson(raffleRequest.getTimeMission());
			// raffle(dto)의 미션타입이 quiz이라면 quizMissionDao를 호출해 quizMission을 생성
		} else if (raffleRequest.getRaffle().getRmissiontype().equals("quiz")) {
			quizMissionDao.updateQuizMisson(raffleRequest.getQuizMission());
		}

	}

	// raffleRequest 객체를 생성 후 래플의 번호와 해당 래플의 미션타입을 불러와 raffleRequest 객체에 반환 후 리턴
	@Transactional
	public RaffleRequest readRaffle(int rno) {
		RaffleRequest raffleRequest = new RaffleRequest();
		raffleRequest.setRaffle(raffleDao.selectByRno(rno));
		if (raffleRequest.getRaffle().getRmissiontype().equals("time")) {
			raffleRequest.setTimeMission(timeMissionDao.selectByRno(rno));
		} else if (raffleRequest.getRaffle().getRmissiontype().equals("quiz")) {
			raffleRequest.setQuizMission(quizMissionDao.selectByRno(rno));
		}

		return raffleRequest;
	}

	public int getRaffleCount() {
		return raffleDao.getRaffleCount(null);
	}
	
	public int getRaffleCountByWord(String word) {
		return raffleDao.getRaffleCountByWord(word);
	}

	public List<Raffle> getRaffleListForAdmin(Pager pager, String word) {
		return raffleDao.selectByPage(pager, word);
	}

	public List<RaffleRequest> getRaffleListForUser(String rcategory, String sortType) {

		return raffleDao.selectByRaffleListForUser(rcategory, sortType);
	}

	@Transactional
	public String insertRaffleDetail(RaffleDetail raffleDetail) {
		String mid = raffleDetail.getMid();
		List<RaffleDetail> list = raffleDetailDao.selectTodayEntryRaffle(mid);
		if(list.size() >= 3) {			
			return "fail";
		} else {
			raffleDetailDao.insertRaffleDetail(raffleDetail);
			if(raffleDetailDao.selectTodayEntryRaffle(mid).size() == 3) {
				Timestamp nowTimestamp = new Timestamp(System.currentTimeMillis());
				BerryHistory berryHistory = new BerryHistory(0, mid, nowTimestamp, 1, "일일 3회 래플 응모 달성");
				memberDao.updateBerry(mid, 1);
				berryHistoryDao.insertBerryHistory(berryHistory);
				return "berry";
			} else {				
				return "success";
			}
		}
	}

	// 당첨자 추첨 로직
	@Transactional
	public String insertWinner(int rno, String mid) {
		// 당첨자를 뽑지 않았을 경우
		if (winnerDao.winnerExistenceCheck(rno) == 0) {
			Raffle raffle = raffleDao.selectByRno(rno);
			List<String> list = pickWinner(rno, raffle.getRwinnercount());
			for (String winner : list) {
				winnerDao.insertWinner(rno, winner);
			}
		}

		if (winnerDao.winnerCheck(rno, mid) != 0) {
			return "당첨";
		} else {
			return "낙첨";
		}
	}

	public RaffleDetail readRaffleDetail(String mid, int rno) {
		return raffleDetailDao.selectRaffleDetail(mid, rno);
	}

	public String readRaffleDetailStatus(String mid, int rno) {
		if (raffleDetailDao.readRaffleDetailStatus(mid, rno) > 0) {
			if (LocalDateTime.now().isAfter(raffleDao.selectByRno(rno).getRfinishedat().toLocalDateTime())) {
				return "당첨 발표";
			} else {
				String missionCleared = raffleDetailDao.selectRaffleDetail(mid, rno).getRdtmissioncleared();
				if (missionCleared.equals("PASS")) {
					return "미션 성공";
				} else if (missionCleared.equals("FAIL")) {
					return "미션 실패";
				} else if (missionCleared.equals("PEND")) {
					return "미션 대기";
				}
			}
		} else {
			if (LocalDateTime.now().isAfter(raffleDao.selectByRno(rno).getRfinishedat().toLocalDateTime())) {
				return "미참여 래플 종료";
			} else {
				return "래플 미참여";
			}
		}
		return "오류";
	}

	public Map<String, Object> getRaffleDetailList(String mid, String role, int pageNo, 
			String status, String startDate, String endDate) {
		Map<String, Object> map = new HashMap<>();

		int myTotalRaffle = raffleDetailDao.selectTotalRaffle(mid, role, startDate, endDate);
		int myOngoingRaffle = raffleDetailDao.selectOngoingRaffle(mid, role, startDate, endDate);
		int myClosedRaffle = raffleDetailDao.selectClosedRaffle(mid, role, startDate, endDate);
		map.put("myTotalRaffle", myTotalRaffle);
		map.put("myOngoingRaffle", myOngoingRaffle);
		map.put("myClosedRaffle", myClosedRaffle);
		Pager pager = new Pager(1, 1, 1, 1);

		if (status.equals("Ongoing")) {
			pager = new Pager(5, 5, myOngoingRaffle, pageNo);
		} else if (status.equals("Closed")) {
			pager = new Pager(5, 5, myClosedRaffle, pageNo);
		} else {
			pager = new Pager(5, 5, myTotalRaffle, pageNo);
		}

		List<RaffleDetailRequest> list = 
				raffleDetailDao.selectRaffleDetailRequest(mid, pager, status, startDate, endDate);
		
		for (RaffleDetailRequest rdr : list) {
			if (rdr.getRaffleDetail().getRdtmissioncleared().equals("PASS"))
				rdr.getRaffleDetail().setRdtmissioncleared("성공");
			else if (rdr.getRaffleDetail().getRdtmissioncleared().equals("FAIL"))
				rdr.getRaffleDetail().setRdtmissioncleared("실패");
			else if (rdr.getRaffleDetail().getRdtmissioncleared().equals("PEND"))
				rdr.getRaffleDetail().setRdtmissioncleared("미진행");

			if (rdr.getRaffle().getRfinishedat().toLocalDateTime().isAfter(LocalDateTime.now()))
				rdr.setNowStatus("진행 중");
			else
				rdr.setNowStatus("종료");

			rdr.setProbability(computeProbability(mid, rdr.getRaffle().getRno(), rdr.getRaffle().getRwinnercount()));
		}
		map.put("RaffleDetailRequest", list);
		map.put("pager", pager);

		return map;
	}

	public List<Winner> readWinnerDetail(int rno, Pager pager) {
		return winnerDao.selectWinnerDetail(rno, pager);
	}

	// 래플 퀴즈 미션 풀기
	@Transactional
	public String updateRdtMissionCleared(int rno, String mid, String manswer) {
		String rMissionType = raffleDao.selectByRno(rno).getRmissiontype();
		if (rMissionType.equals("quiz")) {
			String qanswer = quizMissionDao.selectByRno(rno).getQanswer();
			if (qanswer.equals(manswer)) {
				raffleDetailDao.updateRdtMissionCleard(rno, mid, "PASS");
			} else {
				raffleDetailDao.updateRdtMissionCleard(rno, mid, "FAIL");
			}
			// 미션타입이 time미션일 경우
		} else if (rMissionType.equals("time")) {
			if (raffleDetailDao.checkTimePass(rno, mid) != 0)
				raffleDetailDao.updateTimeMissionCleared(rno, mid);
		}
		
		int cleardMission = 0;
		
		List<RaffleDetail> list = raffleDetailDao.selectTodayEntryRaffle(mid);
		for(RaffleDetail rd : list) {
			if(raffleDetailDao.selectTodayClearedMission(mid, rd.getRno()) > 0)
				cleardMission++;
		}
		
		if(cleardMission == 3) {
			Timestamp nowTimestamp = new Timestamp(System.currentTimeMillis());
			BerryHistory berryHistory = new BerryHistory(0, mid, nowTimestamp, 1, "일일 응모 래플 미션 올 클리어");
			memberDao.updateBerry(mid, 1);
			berryHistoryDao.insertBerryHistory(berryHistory);
			return "berry";
		} else {
			return null;
		}
	}

	public List<RaffleDetail> getMyRaffleDetailRequestList(String mid, String startdate, String enddate) {
		return raffleDetailDao.getMyRaffleDetailRequestList(mid, startdate, enddate);
	}

	public String updateRdtBerrySpend(int rno, String mid, int rdtBerrySpend) {
		int spentberry = raffleDetailDao.selectRdtBerrySpend(rno, mid);
		int mberry = memberDao.selectBerry(mid);
		// spentberry+rdtBerrySpend -한 래플에 사용할수있는 베리는 최대 10개 && 내가 가지고 있는 베리 만큼만 사용 가능
		if (spentberry + rdtBerrySpend <= 10 && rdtBerrySpend <= mberry) {
			raffleDetailDao.updateRdtBerrySpend(rno, mid, rdtBerrySpend);
			memberDao.updateBerry(mid, -rdtBerrySpend);
			BerryHistory berryHistory = new BerryHistory(0, mid, null, -rdtBerrySpend, String.valueOf(rno));
			berryHistoryDao.insertBerryHistory(berryHistory);
			return "성공";
		} else {
			return "실패";
		}
	}

	public List<Raffle> getWinnerDetailList(String mid, Pager pager, String startDate, String endDate) {
		return winnerDao.selectWinnerDetailList(mid, pager, startDate, endDate);
	}

	public RaffleImage getThumbnailImage(int rno) {
		return raffleImageDao.getThumbnailImage(rno);
	}

	public RaffleImage getGiftImage(int rno) {
		return raffleImageDao.getGiftImage(rno);
	}

	public RaffleImage getDetailImage(int rno) {
		return raffleImageDao.getDetailImage(rno);
	}

	public Raffle deleteRaffle(int rno) {
		return raffleDao.deleteRaffle(rno);
	}

	public int getWinRaffleCount(String mid, String startDate, String endDate) {
		return winnerDao.countWinRaffle(mid, startDate, endDate);
	}

	public List<RaffleRequest> searchRaffle(String word) {
		return raffleDao.searchRaffle(word);
	}

	public Map<String, Object> getMemberRaffleDetail(String mid, Raffle raffle) {
		return raffleDao.getMemberRaffleDetail(mid, raffle);
	}

	public List<RaffleDetailRequest> getAdminRaffleDetail(String mid) {
		List<RaffleDetailRequest> list = raffleDetailDao.getAdminRaffleDetail(mid);
		for (RaffleDetailRequest rdr : list) {
			if(winnerDao.winnerCheck(rdr.getRaffle().getRno(), mid) > 0) {
				rdr.setNowStatus("당첨");
			} else {
				rdr.setNowStatus("미당첨");
			}
		}
		return list;
	}

	public List<RaffleRequest> getNewReleaseRaffles() {
		return raffleDao.getNewReleaseRaffles();
	}

	public List<RaffleRequest> getCutOffSoonRaffles() {
		return raffleDao.getCutOffSoonRaffles();
	}

	public Map<String, Object> getAdminDashboard() {
		Map<String, Object> map = new HashMap<>();
		map.put("totalRaffle", raffleDao.getRaffleCount(null));
		map.put("totalSportsRaffle", raffleDao.getRaffleCount("sports"));
		map.put("totalArtRaffle", raffleDao.getRaffleCount("art"));
		map.put("totalFashionRaffle", raffleDao.getRaffleCount("fashion"));

		map.put("totalMember", raffleDetailDao.raffleMemberCount(null));
		map.put("totalSportsMember", raffleDetailDao.raffleMemberCount("sports"));
		map.put("totalArtMember", raffleDetailDao.raffleMemberCount("art"));
		map.put("totalFashionMember", raffleDetailDao.raffleMemberCount("fashion"));

		map.put("ongoingRaffle", raffleDao.ongoingRaffleCount(null));
		map.put("ongoingSportsRaffle", raffleDao.ongoingRaffleCount("sports"));
		map.put("ongoingArtRaffle", raffleDao.ongoingRaffleCount("art"));
		map.put("ongoingFashionRaffle", raffleDao.ongoingRaffleCount("fashion"));

		map.put("ongoingSportsMember", raffleDetailDao.ongoingMemberCount("sports"));
		map.put("ongoingArtMember", raffleDetailDao.ongoingMemberCount("art"));
		map.put("ongoingFashionMember", raffleDetailDao.ongoingMemberCount("fashion"));

		map.put("closedRaffle", raffleDao.closedRaffleCount(null));

		return map;
	}

	public RaffleRequest getRaffleMonitor(int rno) {
		return raffleDao.selectForMonitor(rno);
	}

	public int getMonitorWinCount(int rno) {
		return winnerDao.getRaffleWinnerCount(rno);
	}

	public int countEntryMember(int rno) {
		return raffleDetailDao.countEntryMember(rno);
	}
	
	public int countMyBerry(String mid) {
		return memberDao.selectBerry(mid);
	}

	public List<RaffleDetailRequest> getMemberMonitor(int rno, Pager pager) {
		int winnerCount = winnerDao.getRaffleWinnerCount(rno);
		List<RaffleDetailRequest> list = memberDao.getMonitorList(rno, pager);
		
		for (RaffleDetailRequest rdr : list) {
			if (rdr.getRaffleDetail().getRdtmissioncleared().equals("PASS"))
				rdr.getRaffleDetail().setRdtmissioncleared("성공");
			else if (rdr.getRaffleDetail().getRdtmissioncleared().equals("FAIL"))
				rdr.getRaffleDetail().setRdtmissioncleared("실패");
			else if (rdr.getRaffleDetail().getRdtmissioncleared().equals("PEND"))
				rdr.getRaffleDetail().setRdtmissioncleared("미진행");
			rdr.setProbability(computeProbability(rdr.getRaffleDetail().getMid(), rno, winnerCount));
		}
		
		return list;
	}

	// 확률 계산용
	private String computeProbability(String mid, int rno, int winnerCount) {
		Map<String, BigDecimal> pp = raffleDetailDao.readpp(rno);
		RaffleDetail myProbability = raffleDetailDao.selectRaffleDetail(mid, rno);
		int ppScore = pp.get("TOTALENTRY").intValue() * 10 + pp.get("MISSIONCLEARED").intValue() * 2
				+ pp.get("BERRYSPEND").intValue();
		int missionCleared = (myProbability.getRdtmissioncleared().equals("PASS") ? 2 : 0);
		double myScore = 10 + missionCleared + myProbability.getRdtberryspend();
		myScore = 1 - Math.pow(1 - myScore / ppScore, winnerCount);
		return String.format("%.2f", myScore * 100);
	}

	// 당첨 로직
	private List<String> pickWinner(int rno, int winnerCount) {
		List<String> winner = new ArrayList<>();
		Map<String, BigDecimal> pp = raffleDetailDao.readpp(rno);
		int ppScore = pp.get("TOTALENTRY").intValue() * 10 + pp.get("MISSIONCLEARED").intValue() * 2
				+ pp.get("BERRYSPEND").intValue();
		List<RaffleDetail> raffleDetail = raffleDetailDao.getRaffleDetailList(rno);
		List<EntryMember> entryMember = new ArrayList<>();
		for (int i = 0; i < winnerCount; i++) {
			int index = 0;
			int tempPpScore = ppScore;
			for (RaffleDetail rd : raffleDetail) {
				int missionCleared = (rd.getRdtmissioncleared().equals("PASS") ? 2 : 0);
				int myScore = 10 + missionCleared + rd.getRdtberryspend();
				entryMember.add(new EntryMember(index, rd.getMid(), myScore, tempPpScore, tempPpScore - myScore + 1));
				tempPpScore -= myScore;
				index++;
			}

			int winNum = (int) (Math.random() * ppScore);

			for (EntryMember em : entryMember) {
				if (em.startPoint <= winNum && winNum <= em.endPoint) {
					winner.add(em.mid);
					ppScore -= em.score;
					raffleDetail.remove(em.index);
					entryMember.clear();
					break;
				}
			}
		}
		return winner;
	}

	@AllArgsConstructor
	private class EntryMember {
		int index;
		String mid;
		int score;
		int endPoint;
		int startPoint;
	}
}

package com.mycompany.let_ffle.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.let_ffle.dao.BerryHistoryDao;
import com.mycompany.let_ffle.dao.BoardCommentDao;
import com.mycompany.let_ffle.dao.BoardDao;
import com.mycompany.let_ffle.dao.InquiryDao;
import com.mycompany.let_ffle.dao.LikeListDao;
import com.mycompany.let_ffle.dao.MemberDao;
import com.mycompany.let_ffle.dao.RaffleDao;
import com.mycompany.let_ffle.dao.RaffleDetailDao;
import com.mycompany.let_ffle.dao.WinnerDao;
import com.mycompany.let_ffle.dto.BerryHistory;
import com.mycompany.let_ffle.dto.Board;
import com.mycompany.let_ffle.dto.Inquiry;
import com.mycompany.let_ffle.dto.Member;
import com.mycompany.let_ffle.dto.Pager;
import com.mycompany.let_ffle.dto.RaffleDetail;
import com.mycompany.let_ffle.dto.Winner;
import com.mycompany.let_ffle.dto.request.RaffleDetailRequest;
import com.mycompany.let_ffle.dto.request.RaffleRequest;

@Service
public class MemberService {
	@Autowired
	private RaffleDao raffleDao;
	
	@Autowired
	private MemberDao memberDao;

	@Autowired
	private InquiryDao inquiryDao;

	@Autowired
	private BoardCommentDao boardCommentDao;

	@Autowired
	private BoardDao boardDao;

	@Autowired
	private BerryHistoryDao berryHistoryDao;
	
	@Autowired
	private LikeListDao likeListDao;
	
	@Autowired
	private RaffleDetailDao raffleDetailDao;
	
	@Autowired
	private WinnerDao winnerDao;

	public void join(Member member) {
		memberDao.insert(member);
	}

	public Member selectByMid(String mid) {
		return memberDao.selectByMid(mid);
	}

	public void deleteByMid(String mid) {
		memberDao.delete(mid);
	}

	public void updateMphone(String mid, String mphone) {
		memberDao.updateMphone(mid, mphone);
	}

	public void updateMpassword(String mid, String mpassword) {
		memberDao.updateMpassword(mid, mpassword);
	}

	public void updateMaddress(String mid, String mzipcode, String maddress) {
		memberDao.updateMaddress(mid, mzipcode, maddress);
	}

	public void login(Member member, String mid, String mpassword) {
		memberDao.login(member, mid, mpassword);
	}

	public void insertInquiry(Inquiry inquiry) {
		inquiryDao.insertInquiry(inquiry);
	}

	public int updateInquiry(Inquiry inquiry) {
		return inquiryDao.updateInquiry(inquiry);
	}

	public void updateInquiryReply(int ino, String ireply) {
		inquiryDao.updateInquiryReply(ino, ireply);
	}

	public List<Board> getMyBoardList(Pager pager, String mid) {
		return memberDao.getMyBoardList(pager, mid);
	}

	public int getMyBoardCount(String mid) {
		return memberDao.getMyBoardCount(mid);
	}

	public int getLikeListCount(String mid) {
		return likeListDao.likeListCount(mid);
	}
	
	// 내가 보는 래플 좋아요 여부 확인
	public boolean getLikeStatus(String mid, int rno) {
		int isNull = likeListDao.readLikeStatus(mid, rno);
		if(isNull == 0)
			return false;
		else
			return true;
	}

	// 마이페이지 내 관심 목록 가져오기
	public List<RaffleRequest> getLikeList(Pager pager, String mid) {
		return likeListDao.selectLikeListByMid(pager, mid);
	}

	// 마이페이지 내가 쓴 댓글 갯수
	public int getBoardCommentCount(String mid) {
		return boardCommentDao.getBoardCommentCount(mid);
	}

	// 마이페이지 내가 쓴 댓글 가져오기
	public List<Board> getBoardTitleList(Pager pager, String mid) {
		return boardDao.getBoardTitleList(pager, mid);
	}

	public String selectLoginTime(String mid) {
		Member member = memberDao.selectLoginTime(mid);
		String result;

		Timestamp nowLoginDate = new Timestamp(System.currentTimeMillis());

		if (nowLoginDate.toLocalDateTime().toLocalDate()
				.isAfter(member.getMlastlogintime().toLocalDateTime().toLocalDate())) {
			BerryHistory berryHistory = new BerryHistory(0, mid, nowLoginDate, 1, "매일 최초 로그인");
			memberDao.updateBerry(mid, 1);
			berryHistoryDao.insertBerryHistory(berryHistory);
			result = "berry";
		} else {
			result = "noBerrry";
		}

		/* 최종 로그인 시간 갱신 */
		memberDao.updateLoginTime(mid);
		return result;
	}

	public String findId(Member member) {
		return memberDao.findId(member);
	}

	public int findPassword(String mphone, String mid) {
		return memberDao.findPassword(mphone, mid);
	}

	public void insertAddLikeList(String mid, int rno) {
		likeListDao.insertAddLikeList(mid,rno);
	}

	public void deleteLikeList(String mid, int rno) {
		likeListDao.deleteLikeList(mid,rno);
	}

	public int countByMid(String mid) {
		return memberDao.countByMid(mid);
	}

	public int countByMnickname(String mnickname) {
		return memberDao.countByMnickname(mnickname);
	}
	
	public int countByMphone(String mphone) {
		return memberDao.countByMphone(mphone);
	}

	public Map<String, Object> getMyPageDashboard(String mid) {
		int cleardMission = 0;
		
		List<RaffleDetail> list = raffleDetailDao.selectTodayEntryRaffle(mid);
		
		for(RaffleDetail rd : list) {
			if(raffleDetailDao.selectTodayClearedMission(mid, rd.getRno()) > 0)
				cleardMission++;
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("mNickname", memberDao.selectByMid(mid).getMnickname());
		map.put("todayEntryRaffle", list.size());
		map.put("todayClearedMission", cleardMission);
		
		return map;
	}

	public Map<String, Object> getBerryHistoryList(String mid, int pageNo, String option) {
		int totalBerryHistory = berryHistoryDao.countTotalBH(mid);
		int saveBerryHistory = berryHistoryDao.countSaveBH(mid);
		int useBerryHistory = berryHistoryDao.countUseBH(mid);
		
		Map<String, Object> map = new HashMap<>();
		map.put("totalBerryHistory", totalBerryHistory);
		map.put("saveBerryHistory", saveBerryHistory);
		map.put("useBerryHistory", useBerryHistory);
		
		Pager pager = new Pager();
		if(option.equals("Total")) {
			pager = new Pager(5, 5, totalBerryHistory, pageNo);
		} else if (option.equals("Save")) {
			pager = new Pager(5, 5, saveBerryHistory, pageNo);
		} else if (option.equals("Use")) {
			pager = new Pager(5, 5, useBerryHistory, pageNo);
		}
		map.put("pager", pager);
		
		List<BerryHistory> list = berryHistoryDao.selectByMid(mid, pager, option);
		for(BerryHistory item : list) {
			if(item.getBhchangevalue() < 0)
				item.setBhreason(raffleDao.selectByRno(Integer.parseInt(item.getBhreason())).getRtitle());
		}
		map.put("list", list);
		
		return map;
	}

	public void updateWinner(Winner winner) {
		winnerDao.updateWinner(winner);
	}

	public List<Member> getAdminMemberList(Pager pager, String searchType, String word) {
		return memberDao.selectByMember(pager, searchType, word);
	}

	public int getMemberCount() {
		return memberDao.memberCount();
	}
	
	public int getMemberCountByWord(String searchType, String word) {
		return memberDao.memberCountByWord(searchType, word);
	}
	
	public int getWinnerCount() {
		return winnerDao.winnerCount();
	}

	public List<RaffleDetailRequest> getAdminWinnerList(Pager pager, String searchType, String word) {
		return winnerDao.selectByWinnerList(pager, searchType, word);
	}

	public void updateMnickname(String mid, String mnickname) {
		memberDao.updateMnickname(mid, mnickname);
	}
	
	public int getInquiryCount(String mid) {
		return inquiryDao.getInquiryCount(mid);
	}

	public List<Inquiry> getInquiryList(Pager pager, String mid) {
		return inquiryDao.selectByPage(pager,mid);
	}

	public Inquiry readInquiry(int ino) {
		return inquiryDao.readInquiry(ino);
	}

	public Member getMemberDetail(String mid) {
		return memberDao.getMemberDetail(mid);
	}

	public Member getMember(String mid) {
		return memberDao.getMemberDetail(mid);
	}

	public int getWinnerCountByWord(String searchType, String word) {
		return winnerDao.winnerCountByWord(searchType, word);
	}

	public List<BerryHistory> getBerryHistoryUpToTen(String mid) {
		return berryHistoryDao.getBerryHistoryUpToTen(mid);
	}

	public Inquiry selectInquiryIno(int ino) {
		return inquiryDao.selectInquiryIno(ino);
	}

	public int getUserInquiryCount(String mid, String role) {
		return inquiryDao.getUserInquiryCount(mid,role);
	}

	public List<Inquiry> getUserInquiryList(Pager pager) {
		return inquiryDao.getUserInquiryList(pager);
	}
}
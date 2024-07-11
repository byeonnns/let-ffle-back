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
		// join이라는 변수명을 준 후 MemberDto에서 있는 값을, Memberdao에 insert라는 변수의 담아져 있는 member를
		// 가져와라 라는 의미이다.
	}

	public Member selectByMid(String mid) {
		return memberDao.selectByMid(mid);
	}

	// 회원 탈퇴
	public void deleteByMid(String mid) {
		// TODO Auto-generated method stub
		memberDao.delete(mid);
	}

	// mypage에 휴대폰 번호 변경
	public void updateMphone(String mid, String mphone) {
		// memberDao에서 changeMphone이라는 변수 명을 둔후 MemberDao에 changeMphone이라는 메소드에 있는
		// Member를 가져오라는 의미
		memberDao.updateMphone(mid, mphone);

	}

	public void updateMpassword(String mid, String mpassword) {
		// 받아온 유저의 아이디와 비밀번호를 가지고 memberDao를 호출햐여 데이터 베이스의 비밀번호를 변경하도록 함
		// 쉽게말해 유저의 아이디랑 비밀번호 줄테니까 해당 유저의 비밀번호를 바꿔라 ~
		memberDao.updateMpassword(mid, mpassword);
	}

	public void updateMaddress(String mid, String mzipcode, String maddress) {
		// 컨트롤러에서 받아온 로그인한 유저의 아이디와 주소를 매개변수로 받아 dao를 호출해 데이터베이스에서 주소를 변경하도록 함
		memberDao.updateMaddress(mid, mzipcode, maddress);
	}

	// 여기입둥
	public void login(Member member, String mid, String mpassword) {
		memberDao.login(member, mid, mpassword);
	}

	/* 1:1 문의 */
	public void insertInquiry(Inquiry inquiry) {
		inquiryDao.insertInquiry(inquiry);
	}

	// 문의 수정
	public int updateInquiry(Inquiry inquiry) {
		return inquiryDao.updateInquiry(inquiry);
	}

	// 문의 답변 등록 해주기
	public void updateInquiryReply(int ino, String ireply) {

		inquiryDao.updateInquiryReply(ino, ireply);
	}

	// 마이페이지 게시물 가져오기
	public List<Board> getMyBoardList(Pager pager, String mid) {

		return memberDao.getMyBoardList(pager, mid);
	}

	// 마이페이지 내가 작성한 게시물 갯수
	public int getMyBoardCount(String mid) {

		return memberDao.getMyBoardCount(mid);
	}

	// 마이페이지 내가찜한 리스트의 갯수
	public int getLikeListCount(String mid) {
		return likeListDao.likeListCount(mid);
	}
	
	// 내가 보는 래플 찜 상태 확인
	public boolean getLikeStatus(String mid, int rno) {
		int isNull = likeListDao.readLikeStatus(mid, rno);
		if(isNull == 0)
			return false;
		else
			return true;
	}

	// 마이페이지 내가찜한 리스트 가져오기
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

	public Member selectLoginTime(String mid) {
		Member member = memberDao.selectLoginTime(mid);

		Timestamp nowLoginDate = new Timestamp(System.currentTimeMillis());

		if (nowLoginDate.toLocalDateTime().toLocalDate()
				.isAfter(member.getMlastlogintime().toLocalDateTime().toLocalDate())) {
			BerryHistory berryHistory = new BerryHistory(0, mid, nowLoginDate, 1, "매일 최초 로그인");
			memberDao.updateBerry(mid, 1);
			berryHistoryDao.insertBerryHistory(berryHistory);
		}

		/* 최종 로그인 시간 갱신 */
		memberDao.updateLoginTime(mid);
		return member;
	}

	public String findId(String mphone) {
		return memberDao.findId(mphone);
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
		// TODO Auto-generated method stub
		return memberDao.countByMphone(mphone);
	}

	public Map<String, Object> getMyPageDashboard(String mid) {
		Map<String, Object> map = new HashMap<>();
		int cleardMission = 0;
		map.put("mNickname", memberDao.selectByMid(mid).getMnickname());
		List<RaffleDetail> list = raffleDetailDao.selectTodayEntryRaffle(mid);
		for(RaffleDetail rd : list) {
			if(raffleDetailDao.selectTodayClearedMission(mid, rd.getRno()) > 0)
				cleardMission++;
		}
		map.put("todayEntryRaffle", list.size());
		map.put("todayClearedMission", cleardMission);
		return map;
	}

	public Map<String, Object> getBerryHistoryList(String mid, int pageNo, String option) {
		Map<String, Object> map = new HashMap<>();
		int totalBerryHistory = berryHistoryDao.countTotalBH(mid);
		int saveBerryHistory = berryHistoryDao.countSaveBH(mid);
		int useBerryHistory = berryHistoryDao.countUseBH(mid);
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

	public List<Inquiry> getUserInquiryList(Pager pager, String mid, String role) {
		return inquiryDao.getUserInquiryList(pager,mid,role);
	}

}

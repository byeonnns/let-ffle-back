package com.mycompany.let_ffle.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.let_ffle.dto.RaffleDetail;
import com.mycompany.let_ffle.dto.request.RaffleDetailRequest;

@Mapper
public interface RaffleDetailDao {

	public void insertRaffleDetail(RaffleDetail raffleDetail);

	public RaffleDetail readRaffleDetail(RaffleDetail raffleDetail);

	public List<RaffleDetail> selectRaffleDetailList(@Param("mid") String mid, @Param("role") String role);

	public List<RaffleDetail> getMyRaffleDetailRequestList(String mid, String startdate, String enddate);
	
	// 래플번호(rno)와 회원 아이디(mid), 미션 참여여부(rdtMissionCleared)를 통해 raffleDetail 데이터베이스 정보를 수정( 어떤 회원이 어떤 래플에 참여를 하였는지 안하였는지 )
	// 넘겨줄 매개변수가 2개 이상일 경우 Map을 파라미터 값으로 받음
	public void updateRdtMissionCleard(int rno, String mid, String rdtMissionCleared);

	public void updateRdtBerrySpend(int rno, String mid, int rdtBerrySpend);

	public int selectRdtBerrySpend(int rno, String mid);

	//전체 래플 가져오기
	public int selectTotalRaffle(String mid, String role);
	//현재 진행 중인 래플 가져오기
	public int selectOngoingRaffle(String mid, String role);
	//종료된 래플 가져오기
	public int selectClosedRaffle(String mid, String role);
	
	public List<RaffleDetailRequest> selectRaffleDetailRequest(String mid);

	public Map<String, BigDecimal> readpp(int rno);

	// mid, rno와 일치하는 래플 응모내역 1개 가져오기
	public RaffleDetail selectRaffleDetail(String mid, int rno);

	public int selectTodayEntryRaffle(String mid);

	public int selectTodayClearedMission(String mid);
	
}

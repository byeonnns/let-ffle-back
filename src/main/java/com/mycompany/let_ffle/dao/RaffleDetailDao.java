package com.mycompany.let_ffle.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.let_ffle.dto.RaffleDetail;

@Mapper
public interface RaffleDetailDao {

	public RaffleDetail getRaffleEntryList(String mid);

	public void insertRaffleDetail(RaffleDetail raffleDetail);

	public RaffleDetail readRaffleDetail(RaffleDetail raffleDetail);

	public List<RaffleDetail> selectRaffleDetailList(@Param("mid") String mid, @Param("role") String role);

	// 래플번호(rno)와 회원 아이디(mid), 미션 참여여부(rdtMissionCleared)를 통해 raffleDetail 데이터베이스 정보를 수정( 어떤 회원이 어떤 래플에 참여를 하였는지 안하였는지 )
	// 넘겨줄 매개변수가 2개 이상일 경우 Map을 파라미터 값으로 받음
	public void updateRdtMissionCleard(int rno, String mid, String rdtMissionCleared);

	public List<RaffleDetail> getMyRaffleDetailRequestList(String mid, String startdate, String enddate);
	

	
	

	
	

	
	


	
	
	

	
}

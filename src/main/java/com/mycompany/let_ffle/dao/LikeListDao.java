package com.mycompany.let_ffle.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.let_ffle.dto.Pager;
import com.mycompany.let_ffle.dto.request.RaffleRequest;

@Mapper
public interface LikeListDao {

	public List<RaffleRequest> selectLikeListByMid(Pager pager, String mid);

	public int likeListCount(String mid);
	
	public void insertAddLikeList(String mid, int rno);

	public void deleteLikeList(String mid, int rno);

	public int readLikeStatus(String mid, int rno);
}

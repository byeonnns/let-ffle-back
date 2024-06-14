package com.mycompany.let_ffle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.let_ffle.dao.MemberDao;
import com.mycompany.let_ffle.dto.RaffleDetail;

@Service
public class MemberService {
	@Autowired
	private MemberDao memberDao;

}

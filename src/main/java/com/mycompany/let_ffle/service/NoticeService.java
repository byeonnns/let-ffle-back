package com.mycompany.let_ffle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.let_ffle.dao.NoticeDao;

@Service
public class NoticeService {
	@Autowired
	private NoticeDao noticeDao;
	
}
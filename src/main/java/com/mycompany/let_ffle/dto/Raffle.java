package com.mycompany.let_ffle.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class Raffle {
	private int rno; // 래플 번호
	private String rtitle; // 래플 제목
	private String rsubtitle; // 래플 서브 제목
	private Date rstartedat; // 래플 시작 일자
	private Date rfinishedat; // 래플 종료 일자
	private String rcontent; // 래플 내용
	private String rcategory; // 래플 카테고리
	private int rwinnercount; // 래플 당첨자 수
	private String rmissiontype; // 래플 미션 종류
	private String rgift; // 래플 경품
	
}

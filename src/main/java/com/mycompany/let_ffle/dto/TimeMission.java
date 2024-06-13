package com.mycompany.let_ffle.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class TimeMission {
	private int rno; // 래플 번호
	private Timestamp tstartedat; // 타임 미션 시작 일자
	private Timestamp tfinishedat; // 타임 미션 종료 일자
	
}

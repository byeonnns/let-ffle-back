package com.mycompany.let_ffle.dto;

import lombok.Data;

@Data
public class QuizMission {
	private int rno; // 래플 번호
	private String qcontent; // 퀴즈 내용
	private String qoption1; // 퀴즈 문제 옵션1
	private String qoption2; // 퀴즈 문제 옵션2
	private String qoption3; // 퀴즈 문제 옵션3
	private String qoption4; // 퀴즈 문제 옵션4
}

package com.mycompany.let_ffle.dto;


import java.sql.Timestamp;

import lombok.Data;


@Data
public class RaffleDetail {
	private int rno;
	private String mid;
	private Timestamp rdtcreatedat; // 래플 참여 시간
	private boolean rdtmissioncleared; // 미션 참여 여부
	private int rdtberryspend; // 사용 베리 수
}

package com.mycompany.let_ffle.dto;

import lombok.Data;

@Data
public class Winner {
	private int rno;
	private String mid;
	// 당첨자 정보
	private String wreceivername;
	private String wreceiverzipcode;
	private String wreceiveraddress;
	private String wreceiverphone;
}

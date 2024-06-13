package com.mycompany.let_ffle.dto;

import lombok.Data;

@Data
public class Member {
	private String mid;
	private String mpassword;
	private String mname;
	private String mnickname; // 회원 별명
	private String mphone;
	private String maddress;
	private String mzipcode; // 회원 주소의 우편번호 : 0으로 시작될 수 있어 문자열로 받아야 함
	private String mrole; // ROLE_USER, ROLE_ADMIN 등
	private boolean menabled; // 회원 탈퇴 여부 : false=탈퇴
	private int mberry; // 회원의 보유 베리 개수
}

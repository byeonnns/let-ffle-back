package com.mycompany.let_ffle.dto;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Inquiry {
	private int ino;
	private String mid;
	private String ititle;
	private String icontent;

	// 문의 첨부파일
	private MultipartFile iattach;
	private byte[] iattachdata;
	private String iattachtype;
	private String iattachoname;
	
	private Timestamp icreatedat;
	private Timestamp iupdatedat;
	private String ireply;

}
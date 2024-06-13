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
	private String iattachoname;
	private MultipartFile iattach;
	private byte[] iattachdata;
	private String iattachtype;
	private Timestamp icreatedat;
	private Timestamp iupdatedat;
	private String ireply;
	private String istatus;
	
}

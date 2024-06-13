package com.mycompany.let_ffle.dto;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Board {
	private int bno;
	private String mid;
	private String bcategory;
	private String btitle;
	private String bcontent;
	private MultipartFile battach;
	private String battachtype;
	private String battachoname;
	private byte[] battachdata;
	private int bhitcount;
	private Timestamp bcreatedat;
	private boolean benabled;
	
}

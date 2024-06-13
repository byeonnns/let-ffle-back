package com.mycompany.let_ffle.dto;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Notice {
	private int nno;
	private String ntitle;
	private String ncontent;
	private String nmaincategory;
	private String nsubcategory;
	private String nattachoname;
	private String nattachtype;
	private MultipartFile nattach;
	private byte[] nattachdata;
	private Timestamp ncreatedat;
	private Timestamp nupdatedat;
}

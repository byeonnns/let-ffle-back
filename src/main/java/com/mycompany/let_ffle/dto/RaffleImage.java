package com.mycompany.let_ffle.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class RaffleImage {
	private int rno; 
	
	// 썸네일 이미지
	private MultipartFile rthumbnailattach;
	private byte[] rthumbnailimg;
	private String rthumbnailimgtype;
	private String rthumbnailimgoname;
	
	// 헤드 이미지
	private MultipartFile rheadattach;
	private byte[] rgiftimg;	
	private String rgiftimgtype;
	private String rgiftimgoname;
	
	// 디테일 이미지
	private MultipartFile rdetailattach;
	private byte[] rdetailimg;
	private String rdetailimgtype;
	private String rdetailimgoname;
	
}

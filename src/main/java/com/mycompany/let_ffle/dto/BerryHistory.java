package com.mycompany.let_ffle.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class BerryHistory {
	private int bhno;
	private String mid;
	private Timestamp bhchangedat;
	private int bhchangevalue;
	private String bhreason;
}

package com.mycompany.let_ffle.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BerryHistory {
	private int bhno;
	private String mid;
	private Timestamp bhchangedat;
	private int bhchangevalue;
	private String bhreason;
}

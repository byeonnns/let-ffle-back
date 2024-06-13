package com.mycompany.let_ffle.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Comment {
	private int cno;
	private int bno;
	private String mid;
	private String ccontent;
	private Timestamp ccreatedat;
	private boolean cenabled;
}

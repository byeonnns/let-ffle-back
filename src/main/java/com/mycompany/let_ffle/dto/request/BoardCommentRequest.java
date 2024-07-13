package com.mycompany.let_ffle.dto.request;

import com.mycompany.let_ffle.dto.BoardComment;

import lombok.Data;

@Data
public class BoardCommentRequest {
	private String mnickname;
	private BoardComment boardComment;
}
